import {Component, OnInit} from '@angular/core';
import {PatientRequest} from '../../../dtos/patient';
import {PatientService} from '../../../services/patient.service';
import {TreatsService} from '../../../services/treats.service';
import {debounceTime, distinctUntilChanged, Subject} from 'rxjs';

@Component({
  selector: 'app-doctor-patient-match',
  templateUrl: './request-patient.component.html',
  styleUrls: ['./request-patient.component.scss']
})
export class RequestPatientComponent implements OnInit {

  search = '';
  debouncer = new Subject<any>();

  patients: PatientRequest[] = [];

  constructor(
    private patientService: PatientService,
    private treatsService: TreatsService,
  ) {
  }

  get isEmpty(): boolean {
    return this.patients.length === 0;
  }

  get emptyListText(): string {
    if (this.search === '') {
      return 'No patients found.';
    } else {
      return 'No patients found for <b>' + this.search + '</b>.';
    }
  }

  ngOnInit(): void {
    this.loadPatients();

    this.debouncer.pipe(
      debounceTime(350),
      distinctUntilChanged()).subscribe(
      () => {
        console.log('now searching for: ' + this.search);
        this.loadPatients();
      });
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
  searchChanged() {
    console.log('search', this.search);
    this.debouncer.next(this.search);
  }

  private loadPatients() {
    this.patientService.getAllPatientsToRequest(this.search).subscribe({
      next: (patients: PatientRequest[]) => {
        this.patients = patients.filter(patient => patient.treats === null || patient.treats.status === 'REQUESTED');
        console.log(patients);
      },
      error: error => {
        console.log('Something went wrong while loading patients: ' + error.error.message);
        console.log(error);
      }
    });
  }
}
