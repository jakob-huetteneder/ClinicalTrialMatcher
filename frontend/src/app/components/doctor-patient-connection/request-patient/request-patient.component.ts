import {Component, OnInit} from '@angular/core';
import {Patient, PatientRequest} from '../../../dtos/patient';
import {User} from '../../../dtos/user';
import {PatientService} from '../../../services/patient.service';
import {TreatsService} from '../../../services/treats.service';
import {AuthService} from '../../../services/auth.service';

@Component({
  selector: 'app-doctor-patient-match',
  templateUrl: './request-patient.component.html',
  styleUrls: ['./request-patient.component.scss']
})
export class RequestPatientComponent implements OnInit {

  patients: PatientRequest[] = [];

  constructor(
    private patientService: PatientService,
    private treatsService: TreatsService,
    private authService: AuthService,
  ) {
  }

  ngOnInit(): void {
    this.loadPatients();
  }


  requestUser(patient: PatientRequest) {
    this.treatsService.requestTreats(patient.id).subscribe({
        next: requestedPatient => {
            console.log('Requested patient: ' + patient.firstName + ' ' + patient.lastName);
            // update the patient in the list
            const index = this.patients.findIndex(p => p.id === patient.id);
            this.patients[index] = requestedPatient;
        },
        error: error => {
            console.log('Something went wrong while requesting patient: ' + error.error.message);
            console.log(error);
        }
    });
  }

  private loadPatients() {
    this.patientService.getAllPatientsToRequest().subscribe({
      next: (patients: PatientRequest[]) => {
        this.patients = patients;
        console.log(patients);
      },
      error: error => {
        console.log('Something went wrong while loading patients: ' + error.error.message);
        console.log(error);
      }
    });
  }

}
