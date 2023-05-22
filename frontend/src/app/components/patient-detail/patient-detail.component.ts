import {Component, OnInit} from '@angular/core';
import {Patient} from '../../dtos/patient';
import {ActivatedRoute, Router} from '@angular/router';
import {PatientService} from '../../services/patient.service';

@Component({
  selector: 'app-patient-detail',
  templateUrl: './patient-detail.component.html',
  styleUrls: ['./patient-detail.component.scss']
})
export class PatientDetailComponent implements OnInit{
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

  id = -1;
  edit = false;
  loading = true;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private service: PatientService
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
        },
        error: error => {
          console.error('Error, patient does not exist', error);
          //return this.router.navigate(['']);
        }
      });
    });
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

  pulse(): string {
    return this.loading ? 'animate-pulse' : '';
  }

}
