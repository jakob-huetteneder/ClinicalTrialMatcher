import {Component, OnInit} from '@angular/core';
import {NgModel} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {TrialService} from '../../../services/trial.service';
import {Trial} from '../../../dtos/trial';
import {Gender} from '../../../dtos/gender';
import {Researcher} from '../../../dtos/researcher';
import {ToastrService} from 'ngx-toastr';
import {TrialStatus} from '../../../dtos/trial-status';


@Component({
  selector: 'app-trial-edit',
  templateUrl: './trial-edit.component.html',
  styleUrls: ['./trial-edit.component.scss']
})
export class EditTrialComponent implements OnInit {

  researcher: Researcher = {
    id: 1,
    email: '',
    firstName: '',
    lastName: '',
    password: '',
    status: 1
  };

  trialId = -1;
  trial: Trial = {
    title: '',
    startDate: new Date(),
    endDate: new Date(),
    researcher: this.researcher,
    studyType: '',
    briefSummary: '',
    detailedSummary: '',
    sponsor: '',
    collaborator: '',
    status: TrialStatus.recruiting,
    location: '',
    crGender: Gender.female,
    crMinAge: 0,
    crMaxAge: 0,
    crFreeText: '',
  };

  constructor(
    private service: TrialService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,) {
  }

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.route.params.subscribe(params => this.trialId = params.id);

    });
    this.route.params.subscribe(params => {
      this.trial.id = params.id;
    });
    this.service.getById(this.trialId).subscribe((trial: Trial) => {
      this.trial = trial;
    });
  }

  public editTrial(): void {

    //let observable: Observable<Trial>;
    console.log('trial: ', this.trial);
    this.service.edit(this.trial).subscribe({
      next: data => {
        this.notification.success(`Trial ${this.trial.title} successfully updated.`);
        this.router.navigate(['/trials']);
      },
      error: error => {
        console.error('error editing trial', error);
        this.notification.error(error.error.message);
      }
    });
  }


  public dynamicCssClassesForInput(input: NgModel): any {
    return {
      // This names in this object are determined by the style library,
      // requiring it to follow TypeScript naming conventions does not make sense.
      // eslint-disable-next-line @typescript-eslint/naming-convention
      'is-invalid': !input.valid && !input.pristine,
    };
  }

  public disable(): boolean {
    return (this.trial.title === '' || this.trial.studyType === '' || this.trial.location === '');
  }

  public buttonstyle(): string {
    if (this.trial.title === '' || this.trial.studyType === '' || this.trial.location === '') {
      return 'bg-gray-400';
    } else {
      return 'transition ease-in-out delay-100 duration-300 bg-blue-500 '
        + 'hover:-translate-y-0 hover:scale-110 hover:bg-blue-400 hover:cursor-pointer';
    }
  }
}
