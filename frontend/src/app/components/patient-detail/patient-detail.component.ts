import {Component, OnInit, ViewChildren} from '@angular/core';
import {Patient} from '../../dtos/patient';
import {ActivatedRoute, Router} from '@angular/router';
import {PatientService} from '../../services/patient.service';
import {SafeUrl} from '@angular/platform-browser';
import {FilesService} from '../../services/files.service';

@Component({
  selector: 'app-patient-detail',
  templateUrl: './patient-detail.component.html',
  styleUrls: ['./patient-detail.component.scss']
})
export class PatientDetailComponent implements OnInit{


  @ViewChildren('medicalImages') medicalImages;

  patient: Patient = {
    admissionNote: '',
    diagnoses: [],
    examinations: [],
    firstName: '',
    lastName: '',
    email: '',
    gender: undefined,
    birthdate: undefined
  };
  examinationsImageHidden: boolean[] = [];
  id = -1;
  edit = false;
  loading = true;

  image: SafeUrl = '';
  imageFocus = false;
  imageOriginal: SafeUrl = '';
  imageName = '';


  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private service: PatientService,
    private fileService: FilesService
  ) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.route.params.subscribe(params => {
      this.id = params.id;
      this.service.getById(this.id).subscribe({
        next: data => {
          this.patient = data;
          this.loading = false;
          this.examinationsImageHidden = new Array(this.patient.examinations.length);
          this.examinationsImageHidden.fill(true);
          console.log(this.examinationsImageHidden);
        },
        error: error => {
          console.error('Error, patient does not exist', error);
          //return this.router.navigate(['']);
        }
      });
    });
  }

  getIndexById(id: number): number {
    return this.patient.examinations.findIndex(exam => exam.id === id);
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

  submit() {
    this.service.deleteById(this.id).subscribe({
      next: _data => {
        console.log('Successfully deleted patient {}', this.id);
        this.router.navigate(['']);
      },
      error: error => {
        console.error('Error, patient does not exist', error);
        //return this.router.navigate(['']);
      }
    });
  }

  public load(id: number): string {
    let ret = '';
    this.fileService.getById(id).subscribe({
      next: data => {
        const TYPED_ARRAY = new Uint8Array(data);
        console.log(TYPED_ARRAY);
        const STRING_CHAR = String.fromCharCode.apply(null, TYPED_ARRAY);
        const base64String = btoa(STRING_CHAR);
        ret = 'data:image/png;base64,' + base64String;
        console.log(this.medicalImages._results[this.getIndexById(id)]);
        this.examinationsImageHidden[this.getIndexById(id)] = false;
        this.medicalImages._results[this.getIndexById(id)].nativeElement.setAttribute('src', ret);

      },
      error: error => {
        console.error('Error loading medical image', error);
      }
    });
    return ret;
  }

  pulse(): string {
    return this.loading ? 'animate-pulse' : '';
  }

}
