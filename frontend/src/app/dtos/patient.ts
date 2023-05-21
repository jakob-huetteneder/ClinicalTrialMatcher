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

export interface Examination {
  id?: number;
  date?: Date;
  name: string;
  note: string;
  type: string;
  patientId?: number;
}

export interface Diagnose {
  id?: number;
  date?: Date;
  note: string;
  patientId?: number;
  disease: Disease;
}

export interface Disease {
  id?: number;
  name: string;
  synonyms?: string;
}
