import {Component, Input} from '@angular/core';
import {Trial, TrialStatus} from 'src/app/dtos/trial';
import {TrialListService} from '../../../services/trial-list.service';
import {TrialList} from '../../../dtos/trial-list';
import {ToastrService} from 'ngx-toastr';
import {AuthService} from '../../../services/auth.service';

@Component({
  selector: 'app-trial-list-item',
  templateUrl: './trial-list-item.component.html',
  styleUrls: ['./trial-list-item.component.scss']
})
export class TrialListItemComponent {

  @Input()
  trial: Trial;

  @Input()
  showListSelector = true;

  allLists: TrialList[];

  constructor(
    public authService: AuthService,
    private trialListService: TrialListService,
    private notification: ToastrService,
  ) {
    if (this.authService.isLoggedIn()) {
      this.loadLists();
      this.trialListService.updateEvent.subscribe(() => {
        this.loadLists();
        console.log('TrialList update');
      });
    }
  }


  trialStatus(trial: Trial): string {
    if (trial.status === TrialStatus.recruiting) {
      return 'Recruiting';
    } else if (trial.status === TrialStatus.notRecruiting) {
      return 'Not\xa0recruiting';
    } else if (trial.status === TrialStatus.draft) {
      return 'Draft';
    }
  }

  trialStatusClass(trial: Trial): string {
    if (trial.status === TrialStatus.recruiting) {
      return 'border-green-200 bg-green-100 text-green-800';
    } else if (trial.status === TrialStatus.notRecruiting) {
      return 'border-red-200 bg-red-100 text-red-800';
    } else if (trial.status === TrialStatus.draft) {
      return 'border-gray-200 bg-gray-100 text-gray-800';
    }
  }

  buttonClicked(event: any) {
    event.stopPropagation();
  }

  addTrialToList(trial: Trial, list: TrialList) {
    if (this.listContains(list, trial)) {
      return;
    }
    console.log('addTrialToList', trial, list);
    this.trialListService.addTrialToList(trial, list).subscribe({
      next: data => {
        this.notification.success('Successfully added trial to ' + list.name, 'Success');
        this.loadLists();
      },
      error: error => {
        console.error('Error adding trial to list', error);
        this.notification.error(error.error.message, 'Error adding trial to list');
      }
    });
  }

  listContains(list: TrialList, trial: Trial): boolean {
    return list.trial.some(t => t.id === trial.id);
  }

  deleteTrialFromList(trialId: number, list: TrialList) {
    this.trialListService.deleteTrialFromList(trialId, list).subscribe({
      next: data => {
        this.notification.success('Successfully deleted trial from list', 'Success');
        this.loadLists();
      },
      error: error => {
        console.error('Error deleting trial from list', error);
        this.notification.error(error.error.message, 'Error deleting trial from list');
      }
    });
  }

  private loadLists() {
    this.trialListService.getOwnTrialLists().subscribe({
      next: data => {
        this.allLists = data;
      }
    });
  }
}
