import {Gender} from './gender';
import {User} from './user';


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
