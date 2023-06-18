import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Trial, TrialStatus} from 'src/app/dtos/trial';

@Component({
  selector: 'app-trial-list-item',
  templateUrl: './trial-list-item.component.html',
  styleUrls: ['./trial-list-item.component.scss']
})
export class TrialListItemComponent {

  @Input()
  trial: Trial;

  @Input()
  showDeleteButton = false;

  @Input()
  showEditButton = false;

  @Input()
  showStatisticsButton = false;

  @Output()
  deleteTrial = new EventEmitter<any>();

  @Output()
  editTrial = new EventEmitter<any>();

  @Output()
  showStatistics = new EventEmitter<any>();


  constructor() {
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
}
