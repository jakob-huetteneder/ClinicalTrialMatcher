import {Component, OnInit} from '@angular/core';
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {TrialService} from '../../../services/trial.service';
import {Gender} from '../../../dtos/gender';
import {ToastrService} from 'ngx-toastr';
import {Trial} from '../../../dtos/trial';

export enum TrialCreateEditMode {
  create,
  edit,
}

@Component({
  selector: 'app-trial-create',
  templateUrl: './trial-create-edit.component.html',
  styleUrls: ['./trial-create-edit.component.scss']
})
export class CreateEditTrialComponent implements OnInit {

  trialForm: FormGroup;
  mode: TrialCreateEditMode;

  // update data
  oldTrial: Trial;

  constructor(
    public formBuilder: FormBuilder,
    private trialService: TrialService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
  ) {
  }

  get operation(): string {
    switch (this.mode) {
      case TrialCreateEditMode.create:
        return 'Create';
      case TrialCreateEditMode.edit:
        return 'Edit';
    }
  }

  get inclusionCriteria() {
    return this.trialForm.get('inclusionCriteria') as FormArray;
  }

  get exclusionCriteria() {
    return this.trialForm.get('exclusionCriteria') as FormArray;
  }

  ngOnInit() {

    this.trialForm = this.formBuilder.group({
      title: ['', [Validators.required]],
      startDate: ['', [Validators.required]],
      endDate: ['', [Validators.required]],
      studyType: ['', [Validators.maxLength(255)]],
      briefSummary: ['', [Validators.required]],
      detailedSummary: ['', []],
      sponsor: ['', [Validators.maxLength(255)]],
      collaborator: ['', [Validators.maxLength(255)]],
      status: ['', [Validators.required]],
      location: ['', [Validators.maxLength(255)]],
      crGender: ['', [Validators.required]],
      crMinAge: ['', [Validators.required, Validators.min(0)]],
      crMaxAge: ['', [Validators.required, Validators.min(0)]],
      inclusionCriteria: this.formBuilder.array([]),
      exclusionCriteria: this.formBuilder.array([]),
    }, {validators: this.trialValidator()});

    this.route.data.subscribe({
      next: data => {
        this.mode = data.mode;
        console.log('mode: ', this.mode);
        if (this.mode === TrialCreateEditMode.edit) {
          this.route.params.subscribe({
            next: params => {
              console.log('params: ', params);
              this.trialService.getById(params.id).subscribe({
                next: trial => {
                  this.oldTrial = trial;
                  this.trialForm.addValidators(this.changed());
                  this.trialForm.patchValue(trial);
                  this.trialForm.setControl('inclusionCriteria', this.formBuilder.array(trial.inclusionCriteria));
                  this.trialForm.setControl('exclusionCriteria', this.formBuilder.array(trial.exclusionCriteria));
                },
                error: error => {
                  console.log(error);
                  if (error.status === 404) {
                    this.notification.error('Trial not found.');
                    this.router.navigate(['/researcher/trials']);
                  } else {
                    this.notification.error(error.error.message, error.error.errors);
                  }
                }
              });
            }
          });
        }
      }
    });
  }

  public createTrial(): void {

    if (!this.trialForm.valid) {
      console.log('Invalid input');
      this.notification.error(this.getErrorString());
      return;
    }
    console.log('trialForm: ', this.trialForm.value);
    if (this.mode === TrialCreateEditMode.edit) {
      const trial: Trial = this.trialForm.value;
      trial.id = this.oldTrial.id;
      trial.researcher = this.oldTrial.researcher;
      this.trialService.edit(trial).subscribe({
        next: data => {
          this.notification.success(`Trial ${data.title} successfully updated.`);
          this.router.navigate(['/researcher/trials']);
        },
        error: error => {
          console.error('error updating trial', error);
          this.notification.error(error.error.message, error.error.errors);
        }
      });
      return;
    } else {
      console.log('trialForm: ', this.trialForm.value);
      this.trialService.create(this.trialForm.value).subscribe({
        next: data => {
          this.notification.success(`Trial ${data.title} successfully created.`);
          this.router.navigate(['/researcher/trials']);
        },
        error: error => {
          console.error('error creating trial', error);
          this.notification.error(error.error.message, error.error.errors);
        }
      });
    }
  }

  cancel() {
    this.router.navigate(['/researcher/trials']);
  }

  private trialValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (control.value.startDate > control.value.endDate) {
        return {endDateBeforeStartDate: true};
      }
      if (control.value.crMinAge > control.value.crMaxAge) {
        return {maxAgeSmallerThanMinAge: true};
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
        // if control.value[key] is an array, we need to check each element
        if (Array.isArray(control.value[key])) {
          for (const i in control.value[key]) {
            if (control.value[key][i] !== this.oldTrial[key][i]) {
              unchanged = false;
              console.log('changed: ', key, control.value[key], this.oldTrial[key]);
              return null;
            }
          }
        } else {
          if (control.value[key] !== this.oldTrial[key]) {
            unchanged = false;
            console.log('changed: ', key, control.value[key], this.oldTrial[key]);
            return null;
          }
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
    if (this.trialForm.errors != null) {
      Object.keys(this.trialForm.errors).forEach(keyError => {
        console.log('Key error: ' + keyError);
        errors.push(this.validationErrorName(keyError));
      });
    }
    Object.keys(this.trialForm.controls).forEach(key => {
      const controlErrors: ValidationErrors = this.trialForm.get(key).errors;
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
      case 'minlength':
        return 'is too short';
      case 'maxlength':
        return 'is too long';
      case 'min':
        return 'is too small';
      case 'max':
        return 'is too big';
      case 'endDateBeforeStartDate':
        return 'End date must be after start date';
      case 'maxAgeSmallerThanMinAge':
        return 'Max age must be greater than min age';
      case 'noUpdateRequired':
        return 'No changes made';
      default:
        return 'is invalid';
    }
  }

  private fieldName(key: string): string {
    switch (key) {
      case 'title':
        return 'Title';
      case 'startDate':
        return 'Start date';
      case 'endDate':
        return 'End date';
      case 'studyType':
        return 'Study type';
      case 'briefSummary':
        return 'Brief summary';
      case 'detailedSummary':
        return 'Detailed summary';
      case 'sponsor':
        return 'Sponsor';
      case 'collaborator':
        return 'Collaborator';
      case 'status':
        return 'Status';
      case 'location':
        return 'Location';
      case 'crGender':
        return 'Gender';
      case 'crMinAge':
        return 'Min age';
      case 'crMaxAge':
        return 'Max age';
      case 'crFreeText':
        return 'Additional criteria';
    }
    return '';
  }
}
