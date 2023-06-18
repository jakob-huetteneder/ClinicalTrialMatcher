import {Component, OnInit} from '@angular/core';
import {debounceTime, distinctUntilChanged, Subject} from 'rxjs';
import {Trial, TrialRegistration, TrialRegistrationStatus} from '../../../dtos/trial';
import {TrialService} from '../../../services/trial.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-accept-registration-requests',
  templateUrl: './accept-registration-requests.component.html',
  styleUrls: ['./accept-registration-requests.component.scss']
})
export class AcceptRegistrationRequestsComponent implements OnInit {

  trial = new Trial();

  search = '';
  debouncer = new Subject<any>();

  applied: TrialRegistration[] = [];
  accepted: TrialRegistration[] = [];
  declined: TrialRegistration[] = [];

  constructor(
    private trialService: TrialService,
    private route: ActivatedRoute,
  ) {
  }

  get isEmpty(): boolean {
    return this.applied.length === 0 && this.accepted.length === 0 && this.declined.length === 0;
  }

  get emptyListText(): string {
    if (this.search === '') {
      return 'No registrations found.';
    } else {
      return 'No registrations found for <b>' + this.search + '</b>.';
    }
  }

  ngOnInit(): void {
    this.route.params.subscribe({
      next: params => {
        this.trial.id = params.trialId;

        this.loadRegistrations();

        this.debouncer.pipe(
          debounceTime(350),
          distinctUntilChanged()).subscribe(
          () => {
            console.log('now searching for: ' + this.search);
            this.loadRegistrations();
          });
      },
      error: error => {
        console.log('Something went wrong while loading trial: ' + error.error.message);
      }
    });
  }

  accept(registration: TrialRegistration) {
    this.respondToRequest(registration, true);
  }

  decline(registration: TrialRegistration) {
    this.respondToRequest(registration, false);
  }

  searchChanged(event: any) {
    this.search = event.target.value;
    console.log('search', this.search);
    this.debouncer.next(event);
  }

  private loadRegistrations() {
    this.trialService.getAllRegistrationsForTrial(this.trial.id).subscribe({
      next: (registrations: TrialRegistration[]) => {
        this.applied = registrations.filter(registration => registration.status === TrialRegistrationStatus.patientAccepted);
        this.accepted = registrations.filter(registration => registration.status === TrialRegistrationStatus.accepted);
        this.declined = registrations.filter(registration => registration.status === TrialRegistrationStatus.declined);
        console.log(registrations);
      },
      error: error => {
        console.log('Something went wrong while loading requests: ' + error.error.message);
      }
    });
  }

  private respondToRequest(registrationRequest: TrialRegistration, accept: boolean) {
    this.trialService.respondToRegistrationRequest(registrationRequest.trial.id, registrationRequest.patient.id, accept).subscribe({
      next: () => {
        this.loadRegistrations();
      },
      error: error => {
        console.log('Something went wrong while responding to registration request: ' + error.error.message);
      }
    });
  }
}
