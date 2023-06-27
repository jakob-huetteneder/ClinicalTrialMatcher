import {Component, ElementRef, Renderer2} from '@angular/core';
import {NgModel} from '@angular/forms';
import {Diagnose, Disease, Examination, Patient} from '../../../dtos/patient';
import {DiseaseService} from 'src/app/services/disease.service';
import {PatientService} from 'src/app/services/patient.service';
import {of} from 'rxjs';
import {ToastrService} from 'ngx-toastr';
import {Router} from '@angular/router';
import {AnalyzerService} from '../../../services/analyzer.service';
import {DomSanitizer} from '@angular/platform-browser';
import {Gender} from '../../../dtos/gender';

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
  analyzing = false;
  analyzedText = '';
  highlight = false;
  loading = false;
  constructor(
    private diseaseService: DiseaseService,
    private patientService: PatientService,
    private notification: ToastrService,
    private router: Router,
    private analyzerService: AnalyzerService,
    private renderer: Renderer2, private elementRef: ElementRef,private sanitizer: DomSanitizer
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
      return 'bg-gray-400 hover:cursor-not-allowed';
    } else {
      return 'transition ease-in-out delay-100 duration-300 bg-blue-500 '
        + 'hover:-translate-y-0 hover:scale-110 hover:bg-blue-400 hover:cursor-pointer';
    }
  }

  public disable(): boolean {
    return (this.toRegister.firstName === '' || this.toRegister.lastName === ''
      || this.toRegister.email === '' || this.checkmail !== this.toRegister.email ||
      this.toRegister.examinations.filter(e => e.type === '' || e.name === '' || e.date === undefined).length !== 0 ||
      this.toRegister.birthdate === undefined || this.toRegister.gender === undefined ||
      this.toRegister.diagnoses.filter(d => d.disease.name === '' || d.date === undefined).length !== 0) || this.loading;
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
    this.loading = true;

    this.patientService.createPatient(this.toRegister).subscribe({
      next: () => {
        console.log('Created Patient: ' + this.toRegister.email);
        this.notification.info('Successfully created patient ' + this.toRegister.email);
        this.router.navigate(['']).then();
      },
      error: error => {
        console.log('Something went wrong while creating user: ' + error.error.message);
        this.notification.error(error.error.message, 'Error while creating patient');
        this.loading = false;
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

  sanitizeHTML(htmlString: string): string {
    const nonSpanTagsRegex = /<(?!\/?span\b)[^>]+>/gi;
    return htmlString.replace(nonSpanTagsRegex, '');
  }

  analyze() {
    this.analyzing = true;
    this.analyzerService.analyzeNoteNegatives(this.toRegister.admissionNote).subscribe({
      next: data => {
        console.log(data);

        this.analyzedText = this.toRegister.admissionNote;
        const patternDiseases = new RegExp(data.diseases.join('|'), 'gi');
        const patternNegatives = new RegExp(data.negatives.join('|'), 'gi');
        if(data.diseases.length !== 0) {
          this.analyzedText = this.analyzedText.replace(patternDiseases,
            (match) => `<span class="fw-bold bg-green-300 text-green-800 rounded px-1">${match}</span>`);
        }
        if(data.negatives.length !== 0) {
          this.analyzedText = this.analyzedText.replace(patternNegatives,
            (match) => `<span class="fw-bold bg-red-300 text-red-800 rounded px-1">${match}</span>`);
        }
        console.log(this.analyzedText);
        data.diseases.forEach((d: string) => {
          this.toRegister.diagnoses.push({note: '', disease: {name: d}, date: new Date(Date.now())});
        });
        this.notification.info('Successfully analyzed admission note!');
        this.analyzing = false;
        this.highlight = true;
      },
      error: error => {
        console.log('Error analyzing note: ' + error);
        this.notification.error(error.error.message, 'Error analyzing note');
      }
    });
    this.analyzerService.analyzeGender(this.toRegister.admissionNote).subscribe({
      next: data => {
        if (data === 'm') {
          this.toRegister.gender = Gender.male;
        } else if (data === 'f') {
          this.toRegister.gender = Gender.female;
        }
      }, error: error => {
        console.log('Error analyzing note: ' + error);
        this.notification.error(error.error.message, 'Error analyzing note');
      }
    });
  }
}
