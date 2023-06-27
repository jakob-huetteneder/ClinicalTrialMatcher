import {TrialStatus} from './trial';
import {Gender} from './gender';

export interface Filter {
  gender?: Gender;

  recruiting?: TrialStatus;
  endDate?: Date;
  startDate?: Date;
  minAge?: number;
  maxAge?: number;
  page?: number;
  size?: number;

}
