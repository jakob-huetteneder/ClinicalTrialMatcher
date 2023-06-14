import {Component} from '@angular/core';
import {NgModel} from '@angular/forms';
import {Diagnose, Disease, Examination, Patient} from '../../../dtos/patient';
import {DiseaseService} from 'src/app/services/disease.service';
import {PatientService} from 'src/app/services/patient.service';
import {of} from 'rxjs';
import {ToastrService} from 'ngx-toastr';
import {Router} from '@angular/router';
import {AnalyzerService} from '../../../services/analyzer.service';
import {forEach} from 'lodash';

@Component({
  selector: 'app-register-patient',
  templateUrl: './register-patient.component.html',
  styleUrls: ['./register-patient.component.scss']
})
export class RegisterPatientComponent {
  toRegister: Patient = {
    admissionNote: '',
    diagnoses: [],
    examinations: [],
    firstName: '',
    lastName: '',
    email: '',
    gender: undefined,
    birthdate: undefined
  };
  checkmail = '';

  constructor(
    private diseaseService: DiseaseService,
    private patientService: PatientService,
    private notification: ToastrService,
    private router: Router,
    private analyzerService: AnalyzerService
  ) {
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
    if (this.disable()) {
      return 'bg-gray-400';
    } else {
      return 'transition ease-in-out delay-100 duration-300 bg-blue-500 '
        + 'hover:-translate-y-0 hover:scale-110 hover:bg-blue-400 hover:cursor-pointer';
    }
  }

  public disable(): boolean {
    return  (this.toRegister.firstName === '' || this.toRegister.lastName === ''
      || this.toRegister.email === '' || this.checkmail !== this.toRegister.email ||
      this.toRegister.examinations.filter(e => e.type === '' || e.name === '' || e.date === undefined).length !== 0 ||
      this.toRegister.birthdate === undefined || this.toRegister.gender === undefined ||
      this.toRegister.diagnoses.filter(d => d.disease.name === '' || d.date === undefined).length !== 0);
  }

  public buttonstyleAdmission(): string {
    if (this.toRegister.admissionNote === '') {
      return 'bg-gray-400';
    } else {
      return 'transition ease-in-out delay-100 duration-300 bg-blue-500 '
        + 'hover:-translate-y-0 hover:scale-110 hover:bg-blue-400 hover:cursor-pointer';
    }
  }

  submit() {
    console.log('Create Patient: ' + this.checkmail);

    this.patientService.createPatient(this.toRegister).subscribe({
      next: () => {
        console.log('Created Patient: ' + this.toRegister.email);
        this.notification.info('Successfully created patient ' + this.toRegister.email);
        this.router.navigate(['']).then();
      },
      error: error => {
        console.log('Something went wrong while creating user: ' + error.error.message);
        this.notification.error(error.error.message, 'Error while creating patient');
      }
    });
  }

  add() {
    this.toRegister.diagnoses.push({note: '', disease: {name: ''}});
  }

  remove(diagnose: Diagnose) {
    this.toRegister.diagnoses = this.toRegister.diagnoses.filter(d => d !== diagnose);
  }

  addExam() {
    this.toRegister.examinations.push({name: '', date: new Date(Date.now()), note: '', type: ''});
  }

  removeExam(examination: Examination) {
    this.toRegister.examinations = this.toRegister.examinations.filter(e => e !== examination);
  }

  analyze() {
    this.analyzerService.analyzeNote(this.toRegister.admissionNote).subscribe({
      next: data => {
        console.log(data);
        data.forEach((d: string) => {
          this.toRegister.diagnoses.push({note: '', disease: {name: d}, date: new Date(Date.now())});
        });
        this.notification.info('Successfully analyzed admission note!');
      },
      error: error => {
        console.log('Error analyzing note: ' + error);
        this.notification.error(error.error.message, 'Error analyzing note');
      }
    });
  }
}
