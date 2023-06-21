package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiseaseDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.service.AdmissionNoteAnalyzerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Set;


@Service
public class AdmissionNoteAnalyzerServiceImpl implements AdmissionNoteAnalyzerService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String EXTRACTOR_URL = "http://localhost:5000/";

    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    public AdmissionNoteAnalyzerServiceImpl() {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        this.headers.add("Content-Type", MediaType.TEXT_PLAIN_VALUE);
    }

    @Override
    public Set<DiseaseDto> extractDiseases(String admissionNote) {
        LOG.trace("extractDiseases({})", admissionNote);

        String diseasesUrl = EXTRACTOR_URL + "extract_entities";

        HttpEntity<String> request = new HttpEntity<>(admissionNote, headers);
        ResponseEntity<String[]> diseasesResponse = restTemplate.postForEntity(diseasesUrl, request, String[].class);
        String[] diseasesStrings = diseasesResponse.getBody();

        Set<DiseaseDto> diseases = new HashSet<>();
        for (String disease : diseasesStrings) {

            diseases.add(new DiseaseDto(
                null,
                disease,
                null
            ));
        }
        return diseases;
    }


    @Override
    public Integer extractAge(String admissionNote) {
        LOG.trace("extractAge({})", admissionNote);

        String ageUrl = EXTRACTOR_URL + "extract_age";

        HttpEntity<String> request = new HttpEntity<>(admissionNote, headers);
        ResponseEntity<Integer> ageResponse = restTemplate.postForEntity(ageUrl, request, Integer.class);
        return ageResponse.getBody();
    }


    @Override
    public Gender extractGender(String admissionNote) {
        LOG.trace("extractGender({})", admissionNote);

        String genderUrl = EXTRACTOR_URL + "extract_gender";

        HttpEntity<String> request = new HttpEntity<>(admissionNote, headers);
        ResponseEntity<Character> genderResponse = restTemplate.postForEntity(genderUrl, request, Character.class);
        Character genderChar = genderResponse.getBody();

        if (genderChar == 'm') {
            return Gender.MALE;
        } else if (genderChar == 'f') {
            return Gender.FEMALE;
        } else {
            return Gender.BOTH;
        }
    }
}
