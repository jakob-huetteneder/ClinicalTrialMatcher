import {Gender} from './gender';
import {User} from './user';

export interface Patient {
  id?: number;
  firstName: string;
  lastName: string;
  email: string;
  birthdate: Date;
  gender: Gender;
  admissionNote: string;
  diagnoses: Diagnose[];
  examinations: Examination[];
  doctors?: User[];
}

export class Examination {
  id?: number;
  date?: Date;
  name: string;
  note: string;
  type: string;
  patientId?: number;
}

export class Diagnose {
  id?: number;
  date?: Date;
  note: string;
  patientId?: number;
  disease: Disease;
}

export class Disease {
  id?: number;
  name: string;
  synonyms?: string;
}
