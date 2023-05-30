import {ActivatedRoute, Router} from '@angular/router';
import {Component, OnInit} from '@angular/core';
import {NgModel} from '@angular/forms';
import {Examination} from '../../../dtos/patient';
import {ExaminationService} from 'src/app/services/examination.service';
import {FilesService} from 'src/app/services/files.service';
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';

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
  exam: Examination = {
    date: undefined,
    name: '',
    note: '',
    type: '',
    patientId: undefined
  };
  image: SafeUrl = '';
  imageFocus = false;
  imageOriginal: SafeUrl = '';
  imageName = '';

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private service: ExaminationService,
    private sanitizer: DomSanitizer,
    private fileService: FilesService
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
    this.fileService.getById(this.exam.id).subscribe({
      next: data => {
        const TYPED_ARRAY = new Uint8Array(data);
        const STRING_CHAR = String.fromCharCode.apply(null, TYPED_ARRAY);
        const base64String = btoa(STRING_CHAR);
        this.image = 'data:image/png;base64,' + base64String;
        this.imageOriginal = 'data:image/png;base64,' + base64String;
      },
      error: error => {
        console.error('Error loading medical image', error);
      }
    });
  }

  async submit() {
    const file = await this.convertSafeUrlToFile(this.image, this.imageName).then();
    switch (this.mode) {
      case ExaminationCreateEditMode.create:
        this.service.addNewExamination(this.exam).subscribe({
          next: examinationData => {
            if (this.image !== '') {
              this.exam.id = examinationData.id;
              this.fileService.createImage(file, this.exam.id).subscribe({
                next: _ => {
                  console.log('Created image in backend');

                  this.router.navigate(['/patient/' + this.exam.patientId]);
                },
                error: error => {
                  console.error('Error creating/editing examination', error);
                }
              });
            } else {
              this.router.navigate(['/patient/' + this.exam.patientId]);
            }
          }
        });
        //add image -> add examination
        break;
      case ExaminationCreateEditMode.edit:
        //add image -> update examination
        if (this.image !== this.imageOriginal && this.image === '') {
          this.fileService.deleteById(this.exam.id).subscribe({
            next: __ => {
              console.log('Created image in backend');

              this.service.updateExamination(this.exam).subscribe({
                next: data => {
                  this.router.navigate(['/patient/' + this.exam.patientId]);
                },
                error: error => {
                  console.error('Error creating/editing examination', error);
                }
              });
            },
            error: error => {
              console.error('Error creating image', error);
            }
          });
        } else if (this.image !== this.imageOriginal && this.image !== '') {
          this.fileService.createImage(await file, this.exam.id).subscribe({
            next: __ => {
              console.log('Created image in backend');

              this.service.updateExamination(this.exam).subscribe({
                next: data => {
                  this.router.navigate(['/patient/' + this.exam.patientId]);
                },
                error: error => {
                  console.error('Error creating/editing examination', error);
                }
              });
            },
            error: error => {
              console.error('Error creating image', error);
            }
          });
        } else {
          this.service.updateExamination(this.exam).subscribe({
            next: data => {
              this.router.navigate(['/patient/' + this.exam.patientId]);
            },
            error: error => {
              console.error('Error creating/editing examination', error);
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
    console.log(this.exam);
    this.fileService.deleteById(this.exam.id).subscribe({ // TODO: backend with cascade delete
      next: data => {
        console.log('Deleted image in backend');
        this.service.delete(this.exam.id, this.exam.patientId).subscribe({
          next: () => {
            this.router.navigate(['/patient/' + this.exam.patientId]);
            //this.notification.success(`Horse ${this.horse.name} successfully loaded.`);
          }
        });
      }, error: error => {
        console.log('Image was not deleted', error);
        this.service.delete(this.exam.id, this.exam.patientId).subscribe({
          next: () => {
            this.router.navigate(['/patient/' + this.exam.patientId]);
            //this.notification.success(`Horse ${this.horse.name} successfully loaded.`);
          }
        });
      }
    });
  }

  deleteImage(fileInput: HTMLInputElement) {
    this.imageFocus = false;
    this.image = '';
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
      return;
    }
    const reader = new FileReader();

    reader.onload = (e: any) => {
      this.image = e.target.result;
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
}
