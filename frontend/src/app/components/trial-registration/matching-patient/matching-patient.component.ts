import {Component, OnInit} from '@angular/core';
import {Trial, TrialRegistration} from '../../../dtos/trial';
import {TrialService} from '../../../services/trial.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Patient, Treats, TreatsStatus} from '../../../dtos/patient';
import {debounceTime, distinctUntilChanged, Subject} from 'rxjs';
import {TreatsService} from '../../../services/treats.service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-matching-patient',
  templateUrl: './matching-patient.component.html',
  styleUrls: ['./matching-patient.component.scss']
})
export class MatchingPatientComponent implements OnInit {

  trial = new Trial();
  trialRegistrations: TrialRegistration[] = [];

  patients: Patient[] = [];
  showDetails: boolean[] = [];

  allPatientsOfDoctor: Patient[] = [];
  allMatchingPatients: Patient[] = [];

  search = '';
  debouncer = new Subject<any>();

  constructor(
    private trialService: TrialService,
    private treatsService: TreatsService,
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService,
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
      this.loadRegistrations();
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

  toggleShowDetails(patient: Patient) {
    this.showDetails[this.patients.indexOf(patient)] = !this.showDetails[this.patients.indexOf(patient)];
  }

  isShowDetails(patient: Patient) {
    return this.showDetails[this.patients.indexOf(patient)];
  }

  registerPatient(patient: Patient) {
    this.trialService.registerAsDoctor(this.trial.id, patient.id).subscribe({
      next: () => {
        this.toastr.success('Patient registered for trial');
        this.loadRegistrations();
      },
      error: error => {
        console.log('Could not register patient for trial');
        this.toastr.error('Could not register patient for trial');
      }
    });
  }

  isRegistered(patient: Patient) {
    return this.trialRegistrations.some(registration => registration.patient.id === patient.id);
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

  private loadRegistrations() {
    this.trialService.getAllRegistrationsForTrial(this.trial.id).subscribe({
      next: (registrations: TrialRegistration[]) => {
        this.trialRegistrations = registrations;
      },
      error: error => {
        console.log('Could not load trial registrations');
        this.error('Could not load trial registrations');
      }
    });
  }

  private loadPatients() {
    this.treatsService.getAllRequests('').subscribe({
      next: (patients: Treats[]) => {
        console.log(patients);
        this.allPatientsOfDoctor = patients
          .filter(treats => treats.status === TreatsStatus.accepted)
          .map(treats => treats.patient);
        // this.patients = this.allPatientsOfDoctor;
        this.showDetails = new Array(this.patients.length).fill(false);
        console.log(this.patients);
      },
      error: error => {
        console.log('Could not load patients');
        this.error('Could not load patients');
      }
    });
    this.trialService.getAllMatchingPatients(this.trial.id).subscribe({
      next: (patients: Patient[]) => {
        this.allMatchingPatients = patients;
        this.patients = this.allMatchingPatients;
        console.log('all matching patients', this.allMatchingPatients);
      },
      error: error => {
        console.log('Could not load patients');
        this.error('Could not load patients');
      }
    });
  }

  private searchPatients() {
    this.patients = this.allMatchingPatients.filter(patient =>
      patient.firstName.toLowerCase().includes(this.search.toLowerCase())
      || patient.lastName.toLowerCase().includes(this.search.toLowerCase())
      || (patient.firstName.toLowerCase() + ' ' + patient.lastName.toLowerCase()).includes(this.search.toLowerCase()));
    this.showDetails = new Array(this.patients.length).fill(false);
  }

  private error(errorMsg: string) {
    console.log('Could not load trial');
    // TODO: show error message
    // TODO: navigate back
  }
}
