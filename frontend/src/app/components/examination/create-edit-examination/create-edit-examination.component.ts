import {ActivatedRoute, Router} from '@angular/router';
import {Component, OnInit} from '@angular/core';
import {NgModel} from '@angular/forms';
import {Examination} from '../../../dtos/patient';
import {ExaminationService} from 'src/app/services/examination.service';
import {Observable} from 'rxjs';

export enum ExaminationCreateEditMode {
  create,
  edit,
};

@Component({
  selector: 'app-create-edit-examination',
  templateUrl: './create-edit-examination.component.html',
  styleUrls: ['./create-edit-examination.component.scss']
})
export class CreateEditExaminationComponent implements OnInit {
  mode: ExaminationCreateEditMode = ExaminationCreateEditMode.create;
  exam: Examination = {
    date: undefined,
    name: '',
    note: '',
    type: '',
    patientId: undefined
  };

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private service: ExaminationService
  ) {
  }

  get modeIsCreate(): boolean {
    return this.mode === ExaminationCreateEditMode.create;
  }


  public get heading(): string {
    console.log(this.mode);
    switch (this.mode) {
      case ExaminationCreateEditMode.create:
        return 'Add New Examination';
      case ExaminationCreateEditMode.edit:
        return 'Edit Examination';
      default:
        return '?';
    }
  }

  private get modeActionFinished(): string {
    switch (this.mode) {
      case ExaminationCreateEditMode.create:
        return 'created';
      case ExaminationCreateEditMode.edit:
        return 'edited';
      default:
        return '?';
    }
  }
  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.mode = data.mode;
    });
    this.route.params.subscribe(
      params => {
        this.exam.patientId = params.id;
        if (!this.modeIsCreate) {
          this.exam.id = params.eid;
          this.load();
        }
      });
    console.log(this.exam);
  }
  public dynamicCssClassesForInput(input: NgModel): any {
    return {
      // This names in this object are determined by the style library,
      // requiring it to follow TypeScript naming conventions does not make sense.
      // eslint-disable-next-line @typescript-eslint/naming-convention
      'is-invalid': !input.valid && !input.pristine,
    };
  }

  public tmp(): void {
    console.log(this.exam);
  }

  public buttonstyle(): string {
    if (this.exam.type === '' || this.exam.name === '' || this.exam.note === '' || this.exam.date === undefined) {
      return 'bg-gray-400';
    } else {
      return 'transition ease-in-out delay-100 duration-300 bg-blue-500 '
        + 'hover:-translate-y-0 hover:scale-110 hover:bg-blue-400 hover:cursor-pointer';
    }
  }

  public cancelbuttonstyle(): string {
    if (!(this.exam.type === '' || this.exam.name === '' || this.exam.note === '' || this.exam.date === undefined)) {
      return 'transition ease-in-out delay-100 duration-300 bg-gray-400 '
        + 'hover:-translate-y-0 hover:scale-110 hover:bg-gray-500 hover:cursor-pointer';
    } else {
      return 'transition ease-in-out delay-100 duration-300 bg-blue-500 '
        + 'hover:-translate-y-0 hover:scale-110 hover:bg-blue-400 hover:cursor-pointer';
    }
  }

  public disable(): boolean {
    return this.exam.type === '' || this.exam.name === '' || this.exam.note === '' || this.exam.date === undefined;
  }

  public load(): void {
    console.log('is id valid?', this.exam);
    this.service.load(this.exam.id, this.exam.patientId).subscribe({
      next: data => {
        this.exam = data;
        //this.notification.success(`Horse ${this.horse.name} successfully loaded.`);
      },
      error: error => {
        console.error('Error loading examination', error);
        //this.notification.error(error.error.errors, `Horse ${this.horse.name} could not be loaded`);
      }
    });
  }

  submit() {
    let observable: Observable<Examination>;
    switch (this.mode) {
      case ExaminationCreateEditMode.create:
        observable = this.service.addNewExamination(this.exam);
        break;
      case ExaminationCreateEditMode.edit:
        observable = this.service.updateExamination(this.exam);
        break;
      default:
        console.error('Unknown HorseCreateEditMode', this.mode);
        return;
    }
    observable.subscribe({
      next: data => {
        //this.notification.success(`Examination ${this.exam.name} successfully ${this.modeActionFinished}.`);
        this.router.navigate(['/patient']);
      },
      error: error => {
        console.error('Error creating/editing examination', error);
        //this.notification.error(error.error.errors, `Examination ${this.exam.name} could not be ${this.modeActionFinished}`);
      }
    });
  }

  delete() {
    console.log('is id valid?', this.exam);
    this.service.delete(this.exam.id, this.exam.patientId).subscribe({
      next: data => {
        this.exam = data;
        //this.notification.success(`Horse ${this.horse.name} successfully loaded.`);
      },
      error: error => {
        console.error('Error deleting examination', error);
        //this.notification.error(error.error.errors, `Horse ${this.horse.name} could not be loaded`);
      }
    });
  }
}
