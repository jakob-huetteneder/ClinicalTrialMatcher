package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientRequestDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TreatsDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;

import java.util.List;

public interface TreatsService {

    List<TreatsDto> getAllRequests(long userId, String search);

    PatientRequestDto requestTreats(Long doctorId, Long patientId);

    TreatsDto respondToRequest(long patientUserId, Long doctorId, boolean accepted);

    void doctorTreatsPatient(Doctor doctor, Patient patient);

    void deleteTreats(Long sessionUserId, Long userId);
}
