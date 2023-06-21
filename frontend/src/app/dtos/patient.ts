import {Gender} from './gender';
import {User} from './user';

export class Patient {
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

export interface PatientRequest {
  id?: number;
  firstName: string;
  lastName: string;
  birthdate: Date;
  gender: Gender;
  treats: Treats;
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
  link?: string;
}

export interface Treats {
  id?: number;
  patient: Patient;
  doctor: User;
  status: TreatsStatus;
}

export interface PythonResponse {
  diseases: string[];
  negatives: string[];
}

export enum TreatsStatus {
  requested = 'REQUESTED',
  accepted = 'ACCEPTED',
  declined = 'DECLINED',
}
