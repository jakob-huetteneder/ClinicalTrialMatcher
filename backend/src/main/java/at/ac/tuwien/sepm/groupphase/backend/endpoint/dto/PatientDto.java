package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Diagnose;
import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;


public record PatientDto(
    Long id,
    ApplicationUser applicationUser,
    String firstName,
    String lastName,
    @NotNull(message = "Email must not be null")
    @Email
    String email,
    String admissionNote,
    LocalDate birthdate,
    Gender gender,
    Set<Doctor> doctors,
    Set<Diagnose> diagnoses
) {


}
