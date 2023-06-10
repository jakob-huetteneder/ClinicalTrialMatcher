import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {NgModel} from '@angular/forms';
import {Trial} from '../../dtos/trial';
import {ToastrService} from 'ngx-toastr';
import {TrialService} from '../../services/trial.service';
import {TrialList} from '../../dtos/trial-list';
import {TrialListService} from '../../services/trial-list.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {

  trials: Trial[] = [];
  allLists: TrialList[];

  constructor(public authService: AuthService,
              private trialService: TrialService,
              private notification: ToastrService,
              private trialListService: TrialListService) { }


  ngOnInit() {
    this.reload();
  }

  reload() {
    this.trialService.getAll()
      .subscribe({
        next: data => {
          this.trials = data;
          console.log('data', data);
        },
        error: error => {
          console.error('Error fetching trials', error);
          this.notification.error(error.error.message, 'Error fetching trials');
        }
      });
    this.trialListService.getOwnTrialLists().subscribe({
      next: data => {
        this.allLists = data;
        console.log('allLists: ', this.allLists);
      }
    });
  }
}
