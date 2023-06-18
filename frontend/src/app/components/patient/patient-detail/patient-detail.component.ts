import {Component, OnInit, ViewChildren} from '@angular/core';
import {Patient} from '../../../dtos/patient';
import {ActivatedRoute, Router} from '@angular/router';
import {PatientService} from '../../../services/patient.service';
import {SafeUrl} from '@angular/platform-browser';
import {FilesService} from '../../../services/files.service';
import {ToastrService} from 'ngx-toastr';

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
    private fileService: FilesService,
    private notification: ToastrService
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
          this.examinationsImageHidden.fill(undefined);
          console.log(this.examinationsImageHidden);
        },
        error: error => {
          console.error('Error, patient does not exist', error);
          this.notification.error(error.error.message, 'Error fetching patient');
          return this.router.navigate(['']);
        }
      });
    });
  }

  getIndexById(id: number): number {
    return this.patient.examinations.findIndex(exam => exam.id === id);
  }

  submit() {
    this.service.deleteById(this.id).subscribe({
      next: _data => {
        console.log('Successfully deleted patient {}', this.id);
        this.notification.success('Successfully deleted patient');
        this.router.navigate(['']);
      },
      error: error => {
        console.error('Error, patient does not exist', error);
        this.notification.error(error.error.message, 'Error fetching patient');
        return this.router.navigate(['']);
      }
    });
  }

  public load(id: number): string {
    if (this.examinationsImageHidden[this.getIndexById(id)] !== undefined) {
      this.examinationsImageHidden[this.getIndexById(id)] = !this.examinationsImageHidden[this.getIndexById(id)];
      return '';
    }
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
        //console.error('Error loading medical image', error);
      }
    });
    return ret;
  }

  pulse(): string {
    return this.loading ? 'hover:cursor-not-allowed animate-pulse' : '';
  }

}
