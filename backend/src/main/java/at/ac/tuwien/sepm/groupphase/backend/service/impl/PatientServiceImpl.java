package at.ac.tuwien.sepm.groupphase.backend.service.impl;


import at.ac.tuwien.sepm.groupphase.backend.elasticrepository.PatientSearchRepository;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiagnoseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ExaminationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientRequestDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PatientMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.DiagnoseService;
import at.ac.tuwien.sepm.groupphase.backend.service.ExaminationService;
import at.ac.tuwien.sepm.groupphase.backend.service.PatientService;
import at.ac.tuwien.sepm.groupphase.backend.service.TreatsService;
import jakarta.transaction.Transactional;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQuery;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final UserRepository userRepository;
    private final DiagnoseService diagnoseService;
    private final ExaminationService examinationService;
    private final TreatsService treatsService;
    private final AuthorizationService authorizationService;
    private final PatientSearchRepository patientSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public PatientServiceImpl(PatientRepository patientRepository, PatientMapper patientMapper, UserRepository userRepository,
                              DiagnoseService diagnoseService, ExaminationService examinationService, TreatsService treatsService,
                              AuthorizationService authorizationService, PatientSearchRepository patientSearchRepository,
                              ElasticsearchOperations elasticsearchOperations) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.userRepository = userRepository;
        this.diagnoseService = diagnoseService;
        this.examinationService = examinationService;
        this.treatsService = treatsService;
        this.authorizationService = authorizationService;
        this.elasticsearchOperations = elasticsearchOperations;
        this.patientSearchRepository = patientSearchRepository;
    }

    @Override
    public List<PatientDto> matchPatientsWithTrial(List<String> inclusion, List<String> exclusion, LocalDate minAge, LocalDate maxAge, Gender gender) {
        LOG.trace("matchPatientsWithTrial({}, {}, {}, {}, {})", inclusion, exclusion, minAge.toString(), maxAge.toString(), gender.toString());

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        String minShouldMatch = String.valueOf((100 / (2 * inclusion.size())));

        boolQuery.minimumShouldMatch(minShouldMatch + "%");

        // Range query for birthdate
        RangeQueryBuilder birthdateRangeQuery = QueryBuilders.rangeQuery("birthdate").to(minAge).from(maxAge);
        boolQuery.must(birthdateRangeQuery);

        // Term query for gender
        if (!gender.toString().equals("BOTH")) {
            boolQuery.must(QueryBuilders.matchQuery("gender", gender));
        }

        for (String criterium : inclusion) {

            String[] terms = criterium.split(" ");
            StringBuilder queryBuilder = new StringBuilder();

            if (terms.length > 1) {
                StringBuilder all = new StringBuilder();
                for (int i = 0; i < terms.length; i++) {
                    StringBuilder must = new StringBuilder();
                    List<String> mustList = new ArrayList<>();
                    for (int j = 0; j < terms.length; j++) {
                        if (j != i) {
                            mustList.add(terms[j]);
                        }
                    }
                    for (int j = 0; j < mustList.size(); j++) {
                        if (j == terms.length - 1) {
                            must.append(mustList.get(j)).append(" AND ");
                        } else {
                            must.append(" ").append(mustList.get(j)).append(" ");
                        }
                    }
                    queryBuilder.append("(").append(must).append(")");

                    queryBuilder.append(" OR ");

                    if (i == terms.length - 1) {
                        all.append(" ").append(terms[i]).append(" ");
                    } else {
                        all.append(terms[i]).append(" AND ");
                    }
                }
                queryBuilder.append("(").append(all).append(")");
            } else {
                queryBuilder.append(terms[0]);
            }

            String queryString = queryBuilder.toString().trim();

            boolQuery.should(QueryBuilders.queryStringQuery(queryString)
                .field("diagnoses.disease.name")
                .lenient(true).boost(2));

            boolQuery.should(QueryBuilders.queryStringQuery(queryString)
                .field("admissionNote")
                .lenient(true).defaultOperator(Operator.AND).boost(0.5f));

        }

        for (String criterium : exclusion) {
            boolQuery.mustNot(QueryBuilders.matchQuery("admissionNote", criterium).operator(Operator.AND).boost(0.5f));
            boolQuery.mustNot(QueryBuilders.matchQuery("diagnoses.disease.name", criterium).operator(Operator.AND).boost(2));
        }

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        sourceBuilder.query(boolQuery);

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
            .withSourceFilter(null) // Include all fields in the result
            .withQuery(sourceBuilder.query())
            .build();

        LOG.debug(searchQuery.getQuery().toString());

        // Execute the query and retrieve the search results
        StringQuery stringQuery = new StringQuery(searchQuery.getQuery().toString());
        stringQuery.setMinScore(5.f);
        stringQuery.setMaxResults(100);

        List<Patient> results = elasticsearchOperations
            .search(stringQuery, Patient.class)
            .stream()
            .map(SearchHit::getContent)
            .toList();

        return results.stream()
            .map(patientMapper::patientToPatientDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PatientDto savePatient(PatientDto patient) {
        LOG.trace("savePatient({})", patient);
        ApplicationUser user = userRepository.findById(authorizationService.getSessionUserId()).orElseThrow(() -> new NotFoundException("Logged in user does not exist"));
        if (!(user instanceof Doctor doctor)) {
            throw new NotFoundException("Logged in user needs to be a doctor to create a patient");
        }

        Patient convertedPatient = patientMapper.patientDtoToPatient(patient);
        convertedPatient = patientRepository.save(convertedPatient);

        if (patient.diagnoses() != null) {
            for (DiagnoseDto diagnose : patient.diagnoses()) {
                diagnoseService.addNewDiagnosis(
                    diagnose.withPatientId(convertedPatient.getId())
                );
            }
        }
        if (patient.examinations() != null) {
            for (ExaminationDto examination : patient.examinations()) {
                examinationService.addExamination(
                    examination.withPatientId(convertedPatient.getId())
                );
            }
        }

        treatsService.doctorTreatsPatient(doctor, convertedPatient);

        elasticsearchOperations.save(convertedPatient);
        return patientMapper.patientToPatientDto(convertedPatient);
    }

    @Override
    public PatientDto getById(long id) {
        LOG.trace("getById({})", id);
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isEmpty()) {
            //404 NOT FOUND
            LOG.warn("Patient with id {} does not exist!", id);
            throw new NotFoundException("Patient not found");
        } else {
            LOG.info("Found patient with '{}'", patientMapper.patientToPatientDto(patient.get()));
            return patientMapper.patientToPatientDto(patient.get());
        }
    }

    @Override
    public List<PatientRequestDto> getAllPatientsForDoctorId(Long doctorId, String search) {
        LOG.trace("getAllPatientsForDoctorId({}, {})", doctorId, search);

        if (search == null) {
            search = "";
        }

        List<Patient> patients = patientRepository.findAllContaining(search);
        ApplicationUser user = userRepository.findById(doctorId).orElseThrow(NotFoundException::new);
        if (!(user instanceof Doctor doctor)) {
            throw new NotFoundException();
        }
        return patients.stream().filter(patient -> patient.getApplicationUser() != null).map(patient -> patientMapper.patientToPatientRequestDto(patient, doctor)).toList();
    }

    @Override
    @Transactional
    public PatientDto deleteById(long id) {
        LOG.trace("deleteById({})", id);
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isEmpty()) {
            //404 NOT FOUND
            LOG.warn("Patient with id {} does not exist!", id);
            return null;
        } else {
            patientRepository.deleteById(id);
            patientSearchRepository.deletePatientById(id);
            return patientMapper.patientToPatientDto(patient.get());
        }
    }

    @Override
    public PatientDto synchronizeWithElasticSearchDb(long id) {
        LOG.trace("synchronizeWithElasticSearchDb({})", id);
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isEmpty()) {
            //404 NOT FOUND
            LOG.warn("Patient with id {} does not exist!", id);
            return null;
        } else {
            patient.get().setTreats(null);
            patient.get().setDiagnoses(patient.get().getDiagnoses().stream().peek(
                diagnose -> {
                    diagnose.setPatient(null);
                    diagnose.setDisease(diagnose.getDisease().setId(null));
                    diagnose.setId(null);
                }).collect(Collectors.toSet()));
            patient.get().setExaminations(patient.get().getExaminations().stream().peek(
                examination -> {
                    examination.setPatient(null);
                    examination.setMedicalImage(null);
                }).collect(Collectors.toSet()));
            patientSearchRepository.deletePatientById(id);
            elasticsearchOperations.save(patient.get(), IndexCoordinates.of("patients"));
            return patientMapper.patientToPatientDto(patient.get());
        }
    }
}
