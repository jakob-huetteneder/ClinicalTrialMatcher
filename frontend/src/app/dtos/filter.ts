import {TrialStatus} from './trial';
import {Gender} from './gender';

export interface Filter {
  gender?: Gender;

  recruiting?: TrialStatus;
  endDate?: Date;
  startDate?: Date;
  age?: number;
  page?: number;
  size?: number;

}
