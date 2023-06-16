import {Component, OnInit} from '@angular/core';
import {debounceTime, distinctUntilChanged, Subject} from 'rxjs';
import {TrialRegistration, TrialRegistrationStatus} from '../../../dtos/trial';
import {TrialService} from '../../../services/trial.service';

@Component({
  selector: 'app-view-registration-requests',
  templateUrl: './view-registration-requests.component.html',
  styleUrls: ['./view-registration-requests.component.scss']
})
export class ViewRegistrationRequestsComponent implements OnInit {

  search = '';
  debouncer = new Subject<any>();

  proposed: TrialRegistration[] = [];
  applied: TrialRegistration[] = [];
  accepted: TrialRegistration[] = [];
  declined: TrialRegistration[] = [];

  constructor(
    private trialService: TrialService,
  ) {
  }

  get isEmpty(): boolean {
    return this.applied.length === 0 && this.accepted.length === 0 && this.declined.length === 0 && this.proposed.length === 0;
  }

  get emptyListText(): string {
    if (this.search === '') {
      return 'No registrations found.';
    } else {
      return 'No registrations found for <b>' + this.search + '</b>.';
    }
  }

  ngOnInit(): void {
    this.loadRegistrations();

    this.debouncer.pipe(
      debounceTime(350),
      distinctUntilChanged()).subscribe(
      () => {
        console.log('now searching for: ' + this.search);
        this.loadRegistrations();
      });
  }

  accept(proposal: TrialRegistration) {
    this.respondToProposal(proposal, true);
  }

  decline(proposal: TrialRegistration) {
    this.respondToProposal(proposal, false);
  }

  searchChanged(event: any) {
    this.search = event.target.value;
    console.log('search', this.search);
    this.debouncer.next(event);
  }

  private loadRegistrations() {
    this.trialService.getAllRegistrationsForLoggedInPatient().subscribe({
      next: (registrations: TrialRegistration[]) => {
        this.proposed = registrations.filter(registration => registration.status === TrialRegistrationStatus.proposed);
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

  private respondToProposal(proposal: TrialRegistration, accept: boolean) {
    this.trialService.respondToRegistration(proposal.trial.id, accept).subscribe({
      next: () => {
        this.loadRegistrations();
      },
      error: error => {
        console.log('Something went wrong while responding to proposal: ' + error.error.message);
      }
    });
  }
}
