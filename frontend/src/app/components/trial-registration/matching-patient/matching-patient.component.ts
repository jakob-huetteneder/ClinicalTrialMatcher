import {Component, OnInit} from '@angular/core';
import {Trial} from '../../../dtos/trial';
import {TrialService} from '../../../services/trial.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Patient, PatientRequest, TreatsStatus} from '../../../dtos/patient';
import {PatientService} from '../../../services/patient.service';
import {debounceTime, distinctUntilChanged, Subject} from 'rxjs';

@Component({
  selector: 'app-matching-patient',
  templateUrl: './matching-patient.component.html',
  styleUrls: ['./matching-patient.component.scss']
})
export class MatchingPatientComponent implements OnInit {

  trial = new Trial();
  patients: Patient[] = [];

  allPatients: Patient[] = [];

  search = '';
  debouncer = new Subject<any>();

  constructor(
    private trialService: TrialService,
    private patientService: PatientService,
    private route: ActivatedRoute,
    private router: Router,
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
    this.route.params.subscribe(params => {
      this.trial.id = params.trialId;
      this.loadTrial();
      this.loadPatients();

      this.debouncer.pipe(
        debounceTime(350),
        distinctUntilChanged()).subscribe(
        () => {
          console.log('now searching for: ' + this.search);
          this.searchPatients();
        });
    });
  }

  searchChanged(event: any) {
    this.search = event.target.value;
    console.log('search', this.search);
    this.debouncer.next(event);
  }

  viewPatientDetails(patient: Patient) {
    this.router.navigate(['/doctor/view-patient', patient.id]);
  }

  registerPatient(patient: Patient) {
    // TODO: register patient for trial
  }

  private loadTrial() {
    this.trialService.getById(this.trial.id).subscribe({
      next: (trial: Trial) => {
        this.trial = trial;
      },
      error: error => {
        console.log('Could not load trial');
        this.error('Could not load trial');
      }
    });
  }

  private loadPatients() {
    this.patientService.getAllPatientsToRequest('').subscribe({
      next: (patients: PatientRequest[]) => {
        this.allPatients = patients
          .filter(patient => patient.treats.status === TreatsStatus.accepted)
          .map(patient => patient.treats.patient);
        this.patients = this.allPatients;
        console.log(this.patients);
      },
      error: error => {
        console.log('Could not load patients');
        this.error('Could not load patients');
      }
    });
  }

  private searchPatients() {
    this.patients = this.allPatients.filter(patient =>
      patient.firstName.toLowerCase().includes(this.search.toLowerCase())
      || patient.lastName.toLowerCase().includes(this.search.toLowerCase())
      || (patient.firstName.toLowerCase() + ' ' + patient.lastName.toLowerCase()).includes(this.search.toLowerCase()));
  }

  private error(errorMsg: string) {
    console.log('Could not load trial');
    // TODO: show error message
    // TODO: navigate back
  }
}
