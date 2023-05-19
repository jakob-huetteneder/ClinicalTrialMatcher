import {Component} from '@angular/core';
import {NgModel} from '@angular/forms';
import {Disease, Examination, Patient} from '../../../dtos/patient';

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

  public tmp(): void {
    console.log(this.toRegister);
  }

  public buttonstyle(): string {
    if (this.toRegister.firstName === '' || this.toRegister.lastName === ''
      || this.toRegister.email === '' || this.checkmail !== this.toRegister.email ||
      this.toRegister.examinations.filter(e => e.type === '' || e.name === '' || e.note === '').length !== 0 ||
      this.toRegister.diagnoses.filter(d => d.name === '').length !== 0) {
      return 'bg-gray-400';
    } else {
      return 'transition ease-in-out delay-100 duration-300 bg-blue-500 '
        + 'hover:-translate-y-0 hover:scale-110 hover:bg-blue-400 hover:cursor-pointer';
    }
  }

  public disable(): boolean {
    return (this.toRegister.firstName === '' || this.toRegister.lastName === ''
      || this.toRegister.email === '' || this.checkmail !== this.toRegister.email ||
      this.toRegister.examinations.filter(e => e.type === '' || e.name === '' || e.note === '').length !== 0);
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
    console.log(this.toRegister);
  }

  add() {
    this.toRegister.diagnoses.push({name: ''});
  }

  remove(disease: Disease) {
    this.toRegister.diagnoses = this.toRegister.diagnoses.filter(d => d !== disease);
  }

  addExam() {
    this.toRegister.examinations.push({name: '', date: new Date(Date.now()), note: '', type: ''});
  }

  removeExam(examination: Examination) {
    this.toRegister.examinations = this.toRegister.examinations.filter(e => e !== examination);
  }
}
