import {ActivatedRoute, Router} from '@angular/router';
import {Component, OnInit} from '@angular/core';
import {NgModel} from '@angular/forms';
import {Diagnose, Disease, Examination} from '../../dtos/patient';
import {DiagnoseService} from 'src/app/services/diagnose.service';
import {DiseaseService} from 'src/app/services/disease.service';
import {Observable, of} from 'rxjs';
export enum DiagnoseCreateEditMode {
  create,
  edit,
};

@Component({
  selector: 'app-diagnose',
  templateUrl: './diagnose.component.html',
  styleUrls: ['./diagnose.component.scss']
})
export class DiagnoseComponent implements OnInit{
  mode: DiagnoseCreateEditMode = DiagnoseCreateEditMode.create;
  diagnosis: Diagnose = {
    id: undefined,
    disease: undefined,
    note: '',
    date: undefined,
  };

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private service: DiagnoseService,
    private diseaseService: DiseaseService
  ) {
  }

  get modeIsCreate(): boolean {
    return this.mode === DiagnoseCreateEditMode.create;
  }


  public get heading(): string {
    console.log(this.mode);
    switch (this.mode) {
      case DiagnoseCreateEditMode.create:
        return 'Add New Diagnosis';
      case DiagnoseCreateEditMode.edit:
        return 'Edit Diagnosis';
      default:
        return '?';
    }
  }

  private get modeActionFinished(): string {
    switch (this.mode) {
      case DiagnoseCreateEditMode.create:
        return 'created';
      case DiagnoseCreateEditMode.edit:
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
        this.diagnosis.patientId = params.id;
        if (!this.modeIsCreate) {
          this.diagnosis.id = params.did;
          this.load();
        }
      });
    console.log(this.diagnosis);
  }
  public dynamicCssClassesForInput(input: NgModel): any {
    return {
      // This names in this object are determined by the style library,
      // requiring it to follow TypeScript naming conventions does not make sense.
      // eslint-disable-next-line @typescript-eslint/naming-convention
      'is-invalid': !input.valid && !input.pristine,
    };
  }

  public formatDiseaseName(disease: Disease | null | undefined): string {
    return (disease == null)
      ? ''
      : `${disease.name}`;
  }

  diseaseSuggestions = (input: string) => (input === '')
    ? of([])
    : this.diseaseService.searchByName(input, 5);

  public buttonstyle(): string {
    if (this.diagnosis.disease === undefined || this.diagnosis.note === '' || this.diagnosis.date === undefined) {
      return 'bg-gray-400';
    } else {
      return 'transition ease-in-out delay-100 duration-300 bg-blue-500 '
        + 'hover:-translate-y-0 hover:scale-110 hover:bg-blue-400 hover:cursor-pointer';
    }
  }

  public cancelbuttonstyle(): string {
    if (!(this.diagnosis.disease === undefined || this.diagnosis.note === '' || this.diagnosis.date === undefined)) {
      return 'transition ease-in-out delay-100 duration-300 bg-gray-400 '
        + 'hover:-translate-y-0 hover:scale-110 hover:bg-gray-500 hover:cursor-pointer';
    } else {
      return 'transition ease-in-out delay-100 duration-300 bg-blue-500 '
        + 'hover:-translate-y-0 hover:scale-110 hover:bg-blue-400 hover:cursor-pointer';
    }
  }

  public disable(): boolean {
    return (this.diagnosis.disease === undefined || this.diagnosis.note === '' || this.diagnosis.date === undefined);
  }

  public load(): void {
    console.log('is id valid?', this.diagnosis);
    this.service.load(this.diagnosis.id, this.diagnosis.patientId).subscribe({
      next: data => {
        this.diagnosis = data;
        //this.notification.success(`Horse ${this.horse.name} successfully loaded.`);
      },
      error: error => {
        console.error('Error loading diagnosis', error);
        //this.notification.error(error.error.errors, `Horse ${this.horse.name} could not be loaded`);
      }
    });
  }

  submit() {
    let observable: Observable<Diagnose>;
    switch (this.mode) {
      case DiagnoseCreateEditMode.create:
        observable = this.service.addNewDiagnosis(this.diagnosis);
        break;
      case DiagnoseCreateEditMode.edit:
        observable = this.service.updateDiagnosis(this.diagnosis);
        break;
      default:
        console.error('Unknown DiagnoseCreateEditMode', this.mode);
        return;
    }
    observable.subscribe({
      next: data => {
        //this.notification.success(`Examination ${this.exam.name} successfully ${this.modeActionFinished}.`);
        this.router.navigate(['/patient/' + this.diagnosis.patientId]);
      },
      error: error => {
        console.error('Error creating/editing examination', error);
        //this.notification.error(error.error.errors, `Examination ${this.exam.name} could not be ${this.modeActionFinished}`);
      }
    });
  }

  delete() {
    console.log('is id valid?', this.diagnosis);
    this.service.delete(this.diagnosis.id, this.diagnosis.patientId).subscribe({
      next: data => {
        this.diagnosis = data;
        //this.notification.success(`Horse ${this.horse.name} successfully loaded.`);
      },
      error: error => {
        console.error('Error deleting examination', error);
        //this.notification.error(error.error.errors, `Horse ${this.horse.name} could not be loaded`);
      }
    });
  }
}

