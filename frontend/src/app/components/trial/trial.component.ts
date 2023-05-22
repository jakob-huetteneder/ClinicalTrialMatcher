import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {Trial} from '../../dtos/trial';
import {TrialService} from '../../services/trial.service';


@Component({
  selector: 'app-trial',
  templateUrl: './trial.component.html',
  styleUrls: ['./trial.component.scss']
})
export class TrialComponent implements OnInit {
  trials: Trial[] = [];
  params: Trial = {
    title: '',
    startDate: undefined,
    endDate: undefined,
    briefSummary: '',
    status: '',
    location: '',
    crGender: undefined,
    crMinAge: undefined,
    crMaxAge: undefined,
  };
  constructor(private authService: AuthService,
              private router: Router,
              private trialService: TrialService) {}


  ngOnInit() {
    this.trialService.getAll()
      .subscribe({
        next: data => {
          this.trials = data;
          console.log('data', data);
        },
        error: error => {
          console.error('Error fetching trials', error);
        }
      });
  }

  openForm() {
    document.getElementById('myForm').style.display = 'flex';
  }

  closeForm() {
    document.getElementById('myForm').style.display = 'none';
  }


}
