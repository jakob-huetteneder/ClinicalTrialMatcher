import {Component, OnInit} from '@angular/core';
import {TrialService} from '../../../services/trial.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {Trial, TrialRegistrationStatus} from '../../../dtos/trial';
import {AuthService} from '../../../services/auth.service';
import {Role} from '../../../dtos/role';
import {LinkService} from '../../../services/link.service.';

@Component({
  selector: 'app-trial-detail',
  templateUrl: './trial-detail.component.html',
  styleUrls: ['./trial-detail.component.scss']
})
export class TrialDetailComponent implements OnInit {
  trial = new Trial();

  canRegister = undefined;
  trialApplicationStatus: TrialRegistrationStatus = undefined;

  constructor(
    private trialService: TrialService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
    public authService: AuthService,
    private linkService: LinkService,
  ) {
  }


  get registrationStatusMessage(): string {
    if (this.trialApplicationStatus === null) {
      return 'Loading...';
    }

    switch (this.trialApplicationStatus) {
      case TrialRegistrationStatus.proposed:
        return 'A doctor has invited you to this trial.';
      case TrialRegistrationStatus.patientAccepted:
        return 'A researcher is reviewing your application.';
      case TrialRegistrationStatus.accepted:
        return 'You are registered for this trial.';
      case TrialRegistrationStatus.declined:
        return 'Your application was declined.';
    }
  }

  public get role(): typeof Role {
    return Role;
  }

  ngOnInit(): void {
    this.route.params.subscribe({
      next: params => {
        this.trial.id = params.id;
        this.loadTrial();
        if (this.authService.getUserRole() === Role.patient) {
          this.loadApplicationStatus();
        }
      }
    });
  }

  register(): void {
    this.trialService.registerAsUser(this.trial.id).subscribe({
      next: () => {
        this.canRegister = false;
        this.notification.success('Applied for registration successfully');
        this.loadApplicationStatus();
      },
      error: error => {
        console.error('Error ', error.error.message);
        this.notification.error(error.error.message);
      }
    });
  }


  registrationStatusClass(): string {
    if (this.trialApplicationStatus === TrialRegistrationStatus.proposed) {
      return 'border-orange-200 bg-orange-100 text-orange-800';
    } else if (this.trialApplicationStatus === TrialRegistrationStatus.patientAccepted) {
      return 'border-blue-200 bg-blue-100 text-blue-800';
    } else if (this.trialApplicationStatus === TrialRegistrationStatus.accepted) {
      return 'border-green-200 bg-green-100 text-green-800';
    } else if (this.trialApplicationStatus === TrialRegistrationStatus.declined) {
      return 'border-red-200 bg-red-100 text-red-800';
    }

  }

  getLinkedBriefSummary(): string {
    return this.linkService.filter(this.trial.briefSummary, this.trial.diseases);
  }

  getLinkedDetailedSummary(): string {
    return this.linkService.filter(this.trial.detailedSummary, this.trial.diseases);
  }

  private loadTrial(): void {
    this.trialService.getById(this.trial.id).subscribe({
      next: trial => {
        this.trial = trial;
        console.log(trial);
      },
      error: error => {
        console.error('Error ', error.error.message);
        this.notification.error(error.error.message);
      }
    });
  }

  private loadApplicationStatus() {
    this.trialService.checkIfAlreadyApplied(this.trial.id).subscribe({
      next: trialRegistration => {
        console.log('maybeApplied', trialRegistration);
        this.canRegister = trialRegistration === null;
        this.trialApplicationStatus = trialRegistration?.status;
        console.log('this.applied', this.canRegister);

      },
      error: error => {
        console.error('Error ', error.error.message);
        this.notification.error(error.error.message);
      }
    });
  }
}
