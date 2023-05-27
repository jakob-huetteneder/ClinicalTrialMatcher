package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientRequestDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TreatsDto;

import java.util.List;

public interface TreatsService {

    List<TreatsDto> getAllRequests(long userId);

    PatientRequestDto requestTreats(Long doctorId, Long patientId);

    TreatsDto respondToRequest(long patientUserId, Long doctorId, boolean accepted);

    void deleteTreats(Long sessionUserId, Long userId);
}
