package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientRequestDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TreatsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PatientMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TreatsMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.Treats;
import at.ac.tuwien.sepm.groupphase.backend.entity.TreatsId;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TreatsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.TreatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Set;

@Service
public class TreatsServiceImpl implements TreatsService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final TreatsRepository treatsRepository;
    private final TreatsMapper treatsMapper;

    public TreatsServiceImpl(UserRepository userRepository, PatientRepository patientRepository, PatientMapper patientMapper, TreatsRepository treatsRepository, TreatsMapper treatsMapper) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.treatsRepository = treatsRepository;
        this.treatsMapper = treatsMapper;
    }

    @Override
    public List<TreatsDto> getAllRequests(long userId, String search) {
        LOG.trace("getAllRequests({}, {})", userId, search);
        ApplicationUser user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Logged in user does not exist"));

        if (search == null) {
            search = "";
        }

        Set<Treats> treats;
        if (user instanceof Doctor) {
            treats = treatsRepository.findAllByDoctorId(userId, search);
        } else {
            LOG.debug("User with id {} is not a doctor, searching for patient treats", userId);
            treats = treatsRepository.findAllByPatientId(userId, search);
        }

        LOG.debug("Found {} treating relationships", treats.size());
        return treatsMapper.treatsToTreatsDto(treats);
    }

    @Override
    public PatientRequestDto requestTreats(Long doctorId, Long patientId) {
        LOG.trace("requestTreats({}, {})", doctorId, patientId);
        ApplicationUser user = userRepository.findById(doctorId).orElseThrow();
        if (!(user instanceof Doctor doctor)) {
            throw new IllegalArgumentException("User with id " + doctorId + " is not a doctor");
        }

        Patient patient = patientRepository.findById(patientId).orElseThrow();
        if (patient.getApplicationUser() == null) {
            throw new IllegalArgumentException("Patient with id " + patientId + " has no account and can not be requested");
        }

        Treats treats = new Treats();
        treats.setDoctor(doctor);
        treats.setPatient(patient);
        treats.setStatus(Treats.Status.REQUESTED);

        patient.getTreats().add(treats);
        treatsRepository.save(treats);
        return patientMapper.patientToPatientRequestDto(patient, doctor);
    }

    @Override
    public TreatsDto respondToRequest(long patientUserId, Long doctorId, boolean accepted) {
        LOG.trace("respondToRequest({}, {}, {})", patientUserId, doctorId, accepted);
        Patient patient = patientRepository.findByApplicationUser_Id(patientUserId).orElseThrow();

        LOG.debug("Responding to request from patient {} to doctor {} with {}", patient.getId(), doctorId, accepted);
        // get treats by doctorId and patientId
        Treats treats = treatsRepository.findByTreatsId_PatientIdAndTreatsId_DoctorId(patient.getId(), doctorId).orElseThrow();
        if (accepted) {
            treats.setStatus(Treats.Status.ACCEPTED);
        } else {
            treats.setStatus(Treats.Status.DECLINED);
        }
        return treatsMapper.treatsToTreatsDto(treatsRepository.save(treats));
    }

    @Override
    public void doctorTreatsPatient(Doctor doctor, Patient patient) {
        LOG.trace("doctorTreatsPatient({}, {})", doctor, patient);
        Treats treats = new Treats();
        treats.setDoctor(doctor);
        treats.setPatient(patient);
        treats.setStatus(Treats.Status.ACCEPTED);
        treatsRepository.save(treats);
    }

    @Override
    public void deleteTreats(Long sessionUserId, Long userId) {
        LOG.trace("deleteTreats({}, {})", sessionUserId, userId);
        ApplicationUser sessionUser = userRepository.findById(sessionUserId).orElseThrow();
        if (sessionUser instanceof Doctor) {
            LOG.debug("Deleting treats for doctor {} and patient {}", sessionUserId, userId);
            treatsRepository.deleteById(new TreatsId(userId, sessionUser.getId()));
        } else {
            Patient patient = patientRepository.findByApplicationUser_Id(sessionUserId).orElseThrow();
            LOG.debug("Deleting treats for patient {} and doctor {}", patient.getId(), userId);
            treatsRepository.deleteById(new TreatsId(patient.getId(), userId));
        }
    }
}
