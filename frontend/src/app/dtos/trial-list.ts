import {Trial} from './trial';
import {User} from './user';

export interface TrialList {
  id?: number;
  name: string;
  user: User;
  trial: Trial[];
}
