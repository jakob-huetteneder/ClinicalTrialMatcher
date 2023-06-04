import {ActivatedRoute, Router} from '@angular/router';
import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators} from '@angular/forms';
import {Examination} from '../../../../dtos/patient';
import {ExaminationService} from 'src/app/services/examination.service';
import {FilesService} from 'src/app/services/files.service';
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';
import {ToastrService} from 'ngx-toastr';
import {Observable} from 'rxjs';

export enum ExaminationCreateEditMode {
  create,
  edit,
}

@Component({
  selector: 'app-create-edit-examination',
  templateUrl: './create-edit-examination.component.html',
  styleUrls: ['./create-edit-examination.component.scss']
})
export class CreateEditExaminationComponent implements OnInit {
  mode: ExaminationCreateEditMode = ExaminationCreateEditMode.create;
  examForm: FormGroup;

  id: number;
  patientId: number;
  oldExam: Examination;

  image: SafeUrl = '';
  imageFocus = false;
  imageOriginal: SafeUrl = '';
  imageName = '';

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private fromBuilder: FormBuilder,
    private service: ExaminationService,
    private sanitizer: DomSanitizer,
    private fileService: FilesService,
    private notification: ToastrService
  ) {
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

  ngOnInit(): void {
    this.examForm = this.fromBuilder.group({
      name: ['', [Validators.required, Validators.maxLength(255)]],
      date: ['', [Validators.required]],
      type: ['', [Validators.required, Validators.maxLength(255)]],
      note: ['', [Validators.maxLength(255)]],
      image: [''],
    });

    this.route.data.subscribe(data => {
      this.mode = data.mode;
      this.route.params.subscribe(
        params => {
          this.patientId = params.id;
          if (this.mode === ExaminationCreateEditMode.edit) {
            this.id = params.eid;
            this.load().subscribe({
              next: () => {
                console.log('loaded');
                this.examForm.addValidators(this.changed());
                this.examForm.updateValueAndValidity();
              }
            });
          }
        });
    });

  }

  isEditMode() {
    return this.mode === ExaminationCreateEditMode.edit;
  }

  public load(): Observable<void> {
    // return an observable that will be resolved when all requests are done
    return new Observable<void>(observer => {
      this.service.load(this.id, this.patientId).subscribe({
        next: data => {
          this.oldExam = data;
          this.examForm.patchValue(data);
          observer.next();
        },
        error: error => {
          console.error('Error loading examination', error);
          this.notification.error(error.error.message, 'Error loading examination');
          this.returnToPatient();
        }
      });
      this.fileService.getById(this.id).subscribe({
        next: data => {
          const TYPED_ARRAY = new Uint8Array(data);
          const STRING_CHAR = String.fromCharCode.apply(null, TYPED_ARRAY);
          const base64String = btoa(STRING_CHAR);
          this.image = 'data:image/png;base64,' + base64String;
          this.imageOriginal = 'data:image/png;base64,' + base64String;

          this.examForm.patchValue({image: this.image});
          observer.next();
        },
        error: () => {
          //console.error('Error loading medical image', error);
        }
      });
    });
  }

  async submit() {
    if (this.examForm.invalid) {
      this.notification.error(this.getErrorString());
      return;
    }
    const exam = this.examForm.value;
    exam.id = this.id;
    exam.patientId = this.patientId;

    const file = await this.convertSafeUrlToFile(this.image, this.imageName).then();
    switch (this.mode) {
      case ExaminationCreateEditMode.create:
        this.service.addNewExamination(exam).subscribe({
          next: examinationData => {
            if (this.image !== '') {
              this.id = examinationData.id;
              this.fileService.createImage(file, this.id).subscribe({
                next: _ => {
                  console.log('Created image in backend');

                  this.returnToPatient();
                },
                error: error => {
                  console.error('Error creating/editing examination', error);
                  this.notification.error(error.error.message, 'Error creating examination');
                }
              });
            } else {
              this.notification.info('Successfully created examination');
              this.returnToPatient();
            }
          }
        });
        //add image -> add examination
        break;
      case ExaminationCreateEditMode.edit:
        //add image -> update examination
        if (this.image !== this.imageOriginal && this.image === '') {
          this.fileService.deleteById(this.id).subscribe({
            next: __ => {
              console.log('Created image in backend');

              this.service.updateExamination(exam).subscribe({
                next: data => {
                  this.notification.success('Successfully updated examination');
                  this.returnToPatient();
                },
                error: error => {
                  console.error('Error creating/editing examination', error);
                  this.notification.error(error.error.message, 'Error updating examination');
                }
              });
            },
            error: error => {
              console.error('Error creating image', error);
              this.notification.error(error.error.message, 'Error saving medical image');
            }
          });
        } else if (this.image !== this.imageOriginal && this.image !== '') {
          this.fileService.createImage(await file, this.id).subscribe({
            next: __ => {
              console.log('Created image in backend');

              this.service.updateExamination(exam).subscribe({
                next: data => {
                  this.notification.success('Successfully updated examination');
                  this.returnToPatient();
                },
                error: error => {
                  console.error('Error creating/editing examination', error);
                  this.notification.error(error.error.message, 'Error updating examination');
                }
              });
            },
            error: error => {
              console.error('Error creating image', error);
              this.notification.error(error.error.message, 'Error saving medical image');
            }
          });
        } else {
          this.service.updateExamination(exam).subscribe({
            next: data => {
              this.notification.success('Successfully updated examination');
              this.returnToPatient();
            },
            error: error => {
              console.error('Error creating/editing examination', error);
              this.notification.error(error.error.message, 'Error updating examination');
            }
          });
        }
        break;
      default:
        console.error('Unknown ExaminationCreateEditMode', this.mode);
        return;
    }
  }

  delete() {
    this.fileService.deleteById(this.id).subscribe({ // TODO: backend with cascade delete
      next: data => {
        console.log('Deleted image in backend');
        this.service.delete(this.id, this.patientId).subscribe({
          next: () => {
            this.notification.success('Successfully deleted examination');
            this.returnToPatient();
            //this.notification.success(`Horse ${this.horse.name} successfully loaded.`);
          },
          error: err => {
            this.notification.error(err.error.message, 'Error deleting examination');
          }
        });
      }, error: error => {
        console.log('Image was not deleted', error);
        this.service.delete(this.id, this.patientId).subscribe({
          next: () => {
            this.notification.success('Successfully deleted examination');
            this.returnToPatient();
            //this.notification.success(`Horse ${this.horse.name} successfully loaded.`);
          }, error: err => {
            this.notification.error(err.error.message, 'Error deleting examination');
          }
        });
      }
    });
  }

  returnToPatient() {
    this.router.navigate(['/doctor/view-patient/' + this.patientId]);
  }

  deleteImage(fileInput: HTMLInputElement) {
    this.imageFocus = false;
    this.image = '';
    this.examForm.patchValue({image: ''});
    this.imageName = '';
    fileInput.value = '';
    //this.notification.success(`Horse ${this.horse.name} successfully loaded.`);
  }

  zoomImage(event) {
    this.imageFocus = !this.imageFocus;
    if (this.imageFocus) {
      const image = event.target;
      image.classList.toggle('top-0');
      image.classList.toggle('left-0');
      image.classList.toggle('right-0');
      image.classList.toggle('bottom-0');
      image.classList.toggle('transition-all');
      image.classList.toggle('duration-1100');
    }
  }

  handleTempImageUpload(event: any): void {
    const file = event.target.files[0];
    this.imageName = file.name;

    if (!file.type.match(/image\/(jpeg|png)$/)) {
      console.log('Invalid file type. Please select a JPEG or PNG image.');
      return;
    }
    const maxSizeInBytes = 246300; // maximal uint8array size (uint8 = 8 bit = 1 byte)
    // TODO: adjust to a size of 1-10 MB
    if (file.size > maxSizeInBytes) {
      console.log('File size exceeds the limit. Please select a smaller image.');
      this.notification.info('File size exceeds the limit. Please select a smaller image.');
      return;
    }
    const reader = new FileReader();

    reader.onload = (e: any) => {
      this.image = e.target.result;
      this.examForm.patchValue({
        image: this.image
      });
    };

    reader.readAsDataURL(file);
  }

  convertSafeUrlToFile(safeUrl: SafeUrl, filename: string): Promise<File> {
    return new Promise<File>((resolve, reject) => {
      fetch(safeUrl.toString())
        .then(response => response.blob())
        .then(blob => {
          const file = new File([blob], filename);
          resolve(file);
        })
        .catch(error => {
          reject(error);
        });
    });
  }

  private changed(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      let unchanged = true;
      console.log('test');
      console.log(control.value);
      for (const key in control.value) {
        if (key === 'image') {
          if (control.value[key] !== this.imageOriginal) {
            console.log('changed: ', key, control.value[key], this.imageOriginal);
           unchanged = false;
          }
        } else if (control.value[key] !== this.oldExam[key]) {
          unchanged = false;
          console.log('changed: ', key, control.value[key], this.oldExam[key]);
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
    if (this.examForm.errors != null) {
      Object.keys(this.examForm.errors).forEach(keyError => {
        console.log('Key error: ' + keyError);
        errors.push(this.validationErrorName(keyError));
      });
    }
    Object.keys(this.examForm.controls).forEach(key => {
      const controlErrors: ValidationErrors = this.examForm.get(key).errors;
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
      default:
        return 'is invalid';
    }
  }

  private fieldName(key: string): string {
    switch (key) {
      case 'name':
        return 'Name';
      case 'date':
        return 'Date';
      case 'type':
        return 'Type';
      case 'note':
        return 'Note';
    }
    return '';
  }
}
