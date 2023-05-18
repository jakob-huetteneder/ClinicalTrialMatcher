import {Component, OnInit} from '@angular/core';
import {NgForm, NgModel} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {TrialService} from '../../../services/trial.service';
import {Trial} from '../../../dtos/trial';
import {Gender} from '../../../dtos/gender';
import {Researcher} from '../../../dtos/researcher';
import {Observable} from 'rxjs';
import {ToastrService} from 'ngx-toastr';


@Component({
  selector: 'app-trial-create',
  templateUrl: './trial-create.component.html',
  styleUrls: ['./trial-create.component.scss']
})
export class CreateTrialComponent implements OnInit {

  researcher: Researcher = {
    id: 1,
    email: 'researcher1@email.com',
    firstName: 'Max',
    lastName: 'Meister',
    password: 'chef1234',
    status: 1
  };

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
    status: '',
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
    //private notification: ToastrService,

  ) {
  }

  ngOnInit() {
    console.log('researcher: ', this.researcher);
  }
  public onSubmit(form: NgForm): void {

  }
  public createTrial(): void {

    //let observable: Observable<Trial>;
    console.log('trial: ', this.trial);
    this.service.create(this.trial).subscribe({
      next: data => {
        //this.notification.success(`Trial ${this.trial.title} successfully created.`);
        this.router.navigate(['/trials']);
      },
      error: error => {
        console.error('error creating trial', error);
        //this.notification.error(error.error.message, error.error.errors);

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
}
