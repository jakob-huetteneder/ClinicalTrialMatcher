import {Component, OnInit, ViewChildren} from '@angular/core';
import {Patient} from '../../../dtos/patient';
import {ActivatedRoute, Router} from '@angular/router';
import {PatientService} from '../../../services/patient.service';
import {FilesService} from '../../../services/files.service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-patient-edit',
  templateUrl: './patient-edit.component.html',
  styleUrls: ['./patient-edit.component.scss']
})
export class PatientEditComponent implements OnInit {
  @ViewChildren('medicalImages') medicalImages;

  patient = new Patient();
  examinationsImageHidden: boolean[] = [];
  edit = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private service: PatientService,
    private fileService: FilesService,
    private notification: ToastrService
  ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.patient.id = params.id;
      this.service.getById(this.patient.id).subscribe({
        next: data => {
          this.patient = data;
          this.examinationsImageHidden = new Array(this.patient.examinations.length);
          this.examinationsImageHidden.fill(true);
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
    this.service.deleteById(this.patient.id).subscribe({
      next: _data => {
        console.log('Successfully deleted patient {}', this.patient.id);
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
    if (this.examinationsImageHidden[this.getIndexById(id)] === false) {
      this.examinationsImageHidden[this.getIndexById(id)] = true;
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
}
