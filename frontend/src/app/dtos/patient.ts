import {Gender} from './gender';

export interface Patient {
  id?: number;
  firstName: string;
  lastName: string;
  email: string;
  birthdate: Date;
  gender: Gender;
  admissionNote: string;
  diagnoses: Disease[];
  examinations: Examination[];
}

export interface Examination {
  id?: number;
  date: Date;
  name: string;
  note: string;
  type: string;
  patientId?: number;
}

export interface Disease {
  id?: number;
  name: string;
  synonyms?: string[];
}
