import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {Trial} from '../../dtos/trial';
import {TrialService} from '../../services/trial.service';
import {ToastrService} from 'ngx-toastr';


@Component({
  selector: 'app-trial',
  templateUrl: './trial.component.html',
  styleUrls: ['./trial.component.scss']
})
export class TrialComponent implements OnInit {
  trials: Trial[] = [];
  loading = true;
  size = 10;
  page = 1;

  constructor(private authService: AuthService,
              public router: Router,
              private trialService: TrialService,
              private notification: ToastrService) {
  }

  ngOnInit() {
    this.reload();
  }

  reload() {
    this.trialService.getResearcherTrials()
      .subscribe({
        next: data => {
          this.trials = data;
          console.log('data', data);
        },
        error: error => {
          console.error('Error fetching trials', error);
          this.notification.error(error.error.message, 'Error trials');
        },
        complete: () => {
          this.loading = false;
        }
      });
  }

  delete(trial: Trial): Trial {
    console.log(trial);
    let tmp: Trial;
    this.trialService.deleteTrial(trial.id).subscribe({
      next: data => {
        tmp = data;
        console.log('data', data);
        this.reload();
      },
      error: error => {
        console.error('Error fetching trials', error);
        this.notification.error(error.error.message, 'Error fetching trials');
      }
    });
    return tmp;
  }

  edit(trial: Trial) {
    this.router.navigate(['/researcher/trials/edit', trial.id]).then();
  }
}
