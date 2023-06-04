import {ActivatedRoute, Router} from '@angular/router';
import {Component, OnInit} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';
import {Diagnose, Disease} from '../../../../dtos/patient';
import {DiagnoseService} from 'src/app/services/diagnose.service';
import {DiseaseService} from 'src/app/services/disease.service';
import {Observable, of} from 'rxjs';
import {ToastrService} from 'ngx-toastr';

export enum DiagnoseCreateEditMode {
  create,
  edit,
}

@Component({
  selector: 'app-diagnose',
  templateUrl: './create-edit-diagnose.component.html',
  styleUrls: ['./create-edit-diagnose.component.scss']
})
export class CreateEditDiagnoseComponent implements OnInit {

  diagnosisForm: FormGroup;
  mode: DiagnoseCreateEditMode;

  id: number;
  patientId: number;
  oldDiagnosis: Diagnose;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private service: DiagnoseService,
    private diseaseService: DiseaseService,
    private notification: ToastrService
  ) {
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

  ngOnInit(): void {
    this.diagnosisForm = this.formBuilder.group({
      disease: [new Disease(), [Validators.required]],
      date: ['', [Validators.required]],
      note: ['', [Validators.maxLength(255)]],
    }, {validators: [this.diagnosisValidator()]});

    this.route.data.subscribe(data => {
      this.mode = data.mode;
      this.route.params.subscribe(
        params => {
          this.patientId = params.id;
          if (this.mode === DiagnoseCreateEditMode.edit) {
            this.id = params.did;
            this.load();
            this.diagnosisForm.addValidators(this.changed());
          }
        });
    });
  }

  isEditMode() {
    return this.mode === DiagnoseCreateEditMode.edit;
  }

  public formatDiseaseName(disease: Disease | null | undefined): string {
    return (disease == null || disease.name == null)
      ? ''
      : `${disease.name}`;
  }

  diseaseSuggestions = (input: string) => (input === '')
    ? of([])
    : this.diseaseService.searchByName(input, 5);

  public load(): void {

    this.service.load(this.id, this.patientId).subscribe({
      next: data => {
        this.oldDiagnosis = data;
        this.diagnosisForm.patchValue(data);
      },
      error: error => {
        console.error('Error loading diagnosis', error);
        this.notification.error(error.error.message, 'Error fetching diagnosis');
        this.returnToPatient();
      }
    });
  }

  submit() {
    if (this.diagnosisForm.invalid) {
      this.notification.error(this.getErrorString());
      return;
    }
    console.log(this.diagnosisForm);
    const diagnosis = this.diagnosisForm.value;
    diagnosis.patientId = this.patientId;
    let observable: Observable<Diagnose>;
    switch (this.mode) {
      case DiagnoseCreateEditMode.create:
        observable = this.service.addNewDiagnosis(diagnosis);
        break;
      case DiagnoseCreateEditMode.edit:
        diagnosis.id = this.id;
        observable = this.service.updateDiagnosis(diagnosis);
        break;
      default:
        console.error('Unknown DiagnoseCreateEditMode', this.mode);
        return;
    }
    observable.subscribe({
      next: data => {
        this.notification.success(`Diagnosis successfully saved.`);
        this.returnToPatient();
      },
      error: error => {
        console.error('Error creating/editing diagnosis', error);
        this.notification.error(error.error.message, 'Error saving diagnosis');
      }
    });
  }

  delete() {
    this.service.delete(this.id, this.patientId).subscribe({
      next: data => {
        this.notification.success(`Diagnosis successfully deleted.`);
        this.returnToPatient();
      },
      error: error => {
        console.error('Error deleting diagnosis', error);
        this.notification.error(error.error.message, 'Error deleting diagnosis');
      }
    });
  }

  returnToPatient() {
    this.router.navigate(['/doctor/view-patient/' + this.patientId]);
  }

  private diagnosisValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      console.log('diagnosisValidator', control.value);
      if (control.value.disease == null || control.value.disease.name === '') {
        return {diseaseRequired: true};
      }
      return null;
    };
  }

  private changed(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      let unchanged = true;
      console.log('test');
      console.log(control.value);
      for (const key in control.value) {
        if (control.value[key] !== this.oldDiagnosis[key]) {
          unchanged = false;
          console.log('changed: ', key, control.value[key], this.oldDiagnosis[key]);
          return null;
        }
      }
      if (unchanged) {
        return {noUpdateRequired: true};
      }
      return null;
    };
  }

  private getErrorString() {

    const errors = [];
    if (this.diagnosisForm.errors != null) {
      Object.keys(this.diagnosisForm.errors).forEach(keyError => {
        console.log('Key error: ' + keyError);
        errors.push(this.validationErrorName(keyError));
      });
    }
    Object.keys(this.diagnosisForm.controls).forEach(key => {
      const controlErrors: ValidationErrors = this.diagnosisForm.get(key).errors;
      if (controlErrors != null) {
        Object.keys(controlErrors).forEach(keyError => {
          console.log('Key control: ' + key + ', keyError: ' + keyError);
          errors.push(this.fieldName(key) + ' ' + this.validationErrorName(keyError));
        });
      }
    });
    if (errors.length === 0) {
      return 'Unknown error';
    }
    return errors[0];
  }

  private validationErrorName(keyError: string): string {
    switch (keyError) {
      case 'required':
        return 'is required';
      case 'maxlength':
        return 'is too long';
      case 'noUpdateRequired':
        return 'No changes made';
      case 'diseaseRequired':
        return 'Disease is required';
      default:
        return 'is invalid';
    }
  }

  private fieldName(key: string): string {
    switch (key) {
      case 'disease':
        return 'Disease';
      case 'date':
        return 'Date';
      case 'note':
        return 'Note';
    }
    return '';
  }

}

