import {Gender} from './gender';
import {Researcher} from './researcher';


export interface Trial {
  id?: number;
  title: string;
  startDate: Date;
  endDate: Date;
  researcher?: Researcher;
  studyType?: string;
  briefSummary: string;
  detailedSummary?: string;
  sponsor?: string;
  collaborator?: string;
  status: string;
  location: string;
  crGender: Gender;
  crMinAge: number;
  crMaxAge: number;
  crFreeText?: string;

}
