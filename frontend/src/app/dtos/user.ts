import {Role} from './role';
import {Gender} from './gender';
import {Status} from './status';


export interface User {
  id?: number;
  firstName: string;
  lastName: string;
  email: string;
  password?: string;
  birthdate?: Date;
  gender?: Gender;
  role: Role;
  status: Status;
  admin?: boolean;
}
