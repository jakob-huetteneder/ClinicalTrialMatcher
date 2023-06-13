import {Component, OnInit} from '@angular/core';
import {TrialService} from '../../../services/trial.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {Trial, TrialStatus} from '../../../dtos/trial';
import {AuthService} from '../../../services/auth.service';
import {Role} from '../../../dtos/role';

@Component({
  selector: 'app-trial-detail',
  templateUrl: './trial-detail.component.html',
  styleUrls: ['./trial-detail.component.scss']
})
export class TrialDetailComponent implements OnInit {
  trial: Trial;
  applied: boolean = undefined;

  constructor(
    private trialService: TrialService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
    public authService: AuthService,
  ) {
  }
  public get role(): typeof Role {
    return Role;
  }

  ngOnInit(): void {
    this.route.params.subscribe({
      next: params => {
        console.log(params);
        this.trialService.getById(params.id).subscribe({
          next: trial => {
            console.log(trial);
            this.trial = trial;
            this.trialService.checkIfAlreadyApplied(trial.id).subscribe({
              next: maybeApplied => {
                console.log('maybeApplied', maybeApplied);
                this.applied = maybeApplied;
                console.log('this.applied', this.applied);

              },
              error: error => {
                console.error('Error ', error.error.message);
                this.notification.error(error.error.message);
              }
            });
          },
          error: error => {
            console.log(error);
            if (error.status === 404) {
              this.notification.error('Trial not found.');
            } else {
              this.notification.error(error.error.message, error.error.errors);
            }
          }
        });
      }
    });
  }

  register(trialId: number): void {
    this.trialService.registerAsUser(trialId).subscribe({
      next: () => {
        this.applied = true;
        this.notification.success('Applied for registration successfully');
      },
      error: error => {
        console.error('Error ', error.error.message);
          this.notification.error(error.error.message);
      }
    });
  }

}
