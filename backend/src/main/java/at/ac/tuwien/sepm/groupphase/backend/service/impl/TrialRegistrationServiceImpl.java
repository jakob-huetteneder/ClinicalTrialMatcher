package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TrialRegistrationMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.Registration;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TrialRegistrationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TrialRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.TrialRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

@Service
public class TrialRegistrationServiceImpl implements TrialRegistrationService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final TrialRegistrationRepository trialRegistrationRepository;
    private final TrialRepository trialRepository;
    private final PatientRepository patientRepository;
    private final TrialRegistrationMapper trialRegistrationMapper;
    private final AuthorizationService authorizationService;


    public TrialRegistrationServiceImpl(TrialRegistrationRepository trialRegistrationRepository, TrialRepository trialRepository,
                                        PatientRepository patientRepository, TrialRegistrationMapper trialRegistrationMapper,
                                        AuthorizationService authorizationService) {
        this.trialRegistrationRepository = trialRegistrationRepository;
        this.trialRepository = trialRepository;
        this.patientRepository = patientRepository;
        this.trialRegistrationMapper = trialRegistrationMapper;
        this.authorizationService = authorizationService;
    }

    @Override
    public List<TrialRegistrationDto> getAllRegistrationsForTrial(Long trialId) {
        LOG.info("Getting all registrations for trial with id {}", trialId);
        List<Registration> registrations = trialRegistrationRepository.findAllByTrial_Id(trialId);
        return trialRegistrationMapper.trialRegistrationToTrialRegistrationDto(registrations);
    }

    @Override
    public List<TrialRegistrationDto> getAllRegistrationsForPatient() {
        LOG.info("Getting all registrations for patient");
        Long patientUserId = authorizationService.getSessionUserId();
        Optional<Patient> patient = patientRepository.findByApplicationUser_Id(patientUserId);
        if (patient.isEmpty()) {
            throw new NotFoundException("Patient could not be found");
        }
        List<Registration> registrations = trialRegistrationRepository.findAllByPatient_Id(patient.get().getId());
        return trialRegistrationMapper.trialRegistrationToTrialRegistrationDto(registrations);
    }

    @Override
    public TrialRegistrationDto requestRegistrationAsPatient(Long trialId) {
        Long patientUserId = authorizationService.getSessionUserId();
        Optional<Patient> patient = patientRepository.findByApplicationUser_Id(patientUserId);
        if (patient.isEmpty()) {
            throw new NotFoundException("Patient could not be found");
        }

        Registration registration = new Registration();
        registration.setTrial(trialRepository.findById(trialId).orElseThrow(() -> new NotFoundException("Trial could not be found")));
        registration.setPatient(patient.get());
        registration.setStatus(Registration.Status.PATIENT_ACCEPTED);
        registration = trialRegistrationRepository.save(registration);

        return trialRegistrationMapper.trialRegistrationToTrialRegistrationDto(registration);

    }

    @Override
    public TrialRegistrationDto requestRegistrationAsDoctor(Long patientId, Long trialId) {
        Optional<Patient> patient = patientRepository.findById(patientId);

        if (patient.isEmpty()) {
            throw new NotFoundException("Patient could not be found");
        }

        Registration registration = new Registration();
        registration.setTrial(trialRepository.findById(trialId).orElseThrow(() -> new NotFoundException("Trial could not be found")));
        registration.setPatient(patient.get());
        if (patient.get().getApplicationUser() == null) {
            registration.setStatus(Registration.Status.PATIENT_ACCEPTED);
        } else {
            registration.setStatus(Registration.Status.PROPOSED);
        }
        registration = trialRegistrationRepository.save(registration);

        return trialRegistrationMapper.trialRegistrationToTrialRegistrationDto(registration);
    }

    @Override
    public TrialRegistrationDto respondToRegistrationRequestProposal(Long trialId, boolean accepted) {
        LOG.info("Responding to registration request proposal for trial with id {} ({})", trialId, accepted ? "accepted" : "rejected");
        Long patientUserId = authorizationService.getSessionUserId();
        Optional<Patient> patient = patientRepository.findByApplicationUser_Id(patientUserId);
        if (patient.isEmpty()) {
            throw new NotFoundException("Patient could not be found");
        }

        Optional<Registration> registration = trialRegistrationRepository.findByRegistrationId_PatientIdAndRegistrationId_TrialId(patient.get().getId(), trialId);
        if (registration.isPresent() && registration.get().getStatus() == Registration.Status.PROPOSED) {
            Registration r = registration.get();
            r.setStatus(accepted ? Registration.Status.PATIENT_ACCEPTED : Registration.Status.DECLINED);
            r = trialRegistrationRepository.save(r);
            return trialRegistrationMapper.trialRegistrationToTrialRegistrationDto(r);
        } else {
            throw new NotFoundException("No registration found that needs action.");
        }
    }

    @Override
    public TrialRegistrationDto respondToRegistrationRequest(Long patientId, Long trialId, boolean accepted) {
        LOG.info("Responding to registration of patient with id {} for trial with id {} ({})", patientId, trialId, accepted ? "accepted" : "rejected");
        Optional<Registration> registration = trialRegistrationRepository.findByRegistrationId_PatientIdAndRegistrationId_TrialId(patientId, trialId);
        if (registration.isPresent() && registration.get().getStatus() == Registration.Status.PATIENT_ACCEPTED) {
            Registration r = registration.get();
            r.setStatus(accepted ? Registration.Status.ACCEPTED : Registration.Status.DECLINED);
            r = trialRegistrationRepository.save(r);
            return trialRegistrationMapper.trialRegistrationToTrialRegistrationDto(r);
        } else {
            throw new NotFoundException("No registration found that needs action.");
        }
    }

    @Override
    public TrialRegistrationDto checkIfAlreadyRegistered(Long trialId) {
        Long patientUserId = authorizationService.getSessionUserId();
        Optional<Patient> patient = patientRepository.findByApplicationUser_Id(patientUserId);
        if (patient.isEmpty()) {
            throw new NotFoundException("Patient could not be found");
        }

        Optional<Registration> registration = trialRegistrationRepository.findByRegistrationId_PatientIdAndRegistrationId_TrialId(patient.get().getId(), trialId);

        return registration.map(trialRegistrationMapper::trialRegistrationToTrialRegistrationDto).orElse(null);
    }
}
