import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../../services/auth.service';
import {TrialService} from '../../../services/trial.service';
import {ToastrService} from 'ngx-toastr';
import {TrialListService} from '../../../services/trial-list.service';
import {TrialList} from '../../../dtos/trial-list';
import {ActivatedRoute} from '@angular/router';
import {Trial} from '../../../dtos/trial';


@Component({
  selector: 'app-trial-list',
  templateUrl: './trial-list.component.html',
  styleUrls: ['./trial-list.component.scss']
})
export class TrialListComponent implements OnInit {

  //declare trialList with empty trials array
  trialList: TrialList = {id: 3, name: '', user: null, trial: []};
  trials: Trial[] = [];
  page = 1;
  size = 10;

  constructor(public authService: AuthService,
              private trialService: TrialService,
              private notification: ToastrService,
              private trialListService: TrialListService,
              private route: ActivatedRoute) {
    }

  ngOnInit() {
    this.reload();
  }

  reload() {
    this.route.params.subscribe(params => {
      this.trialListService.getTrialListById(params['id']).subscribe({
        next: data => {
          this.trialList = data;
          console.log('data', data);
        },
        error: error => {
          console.error('Error fetching trials', error);
          this.notification.error(error.error.message, 'Error fetching trials');
        }
      });
    });
  }

  deleteTrialFromList(id: number) {
    this.trialListService.deleteTrialFromList(id, this.trialList).subscribe({
      next: data => {
        this.notification.success('Successfully deleted trial from list', 'Success');
        this.reload();
      },
      error: error => {
        console.error('Error deleting trial from list', error);
        this.notification.error(error.error.message, 'Error deleting trial from list');
      }
    });
  }


}
