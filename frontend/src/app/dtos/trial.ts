import {Gender} from './gender';
import {User} from './user';
import {Patient} from './patient';

export class Trial {
  id?: number;
  title: string;
  startDate: Date;
  endDate: Date;
  researcher: User;
  studyType: string;
  briefSummary: string;
  detailedSummary: string;
  sponsor: string;
  collaborator: string;
  status: TrialStatus;
  location: string;
  crGender: Gender;
  crMinAge: number;
  crMaxAge: number;
  inclusionCriteria: string[];
  exclusionCriteria: string[];

}

export enum TrialStatus {
  recruiting = 'RECRUITING',
  notRecruiting = 'NOT_RECRUITING',
  draft = 'DRAFT',
}

export class TrialRegistration {
  id?: number;
  patient: Patient;
  trial: Trial;
  status: TrialRegistrationStatus;
  date: Date;
}

export enum TrialRegistrationStatus {
  proposed = 'PROPOSED', // doctor proposes patient to trial
  patientAccepted = 'PATIENT_ACCEPTED', // patient accepted trial
  accepted = 'ACCEPTED',
  declined = 'DECLINED',
}
