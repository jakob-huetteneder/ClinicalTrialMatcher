import {Role} from './role';
import {Gender} from './gender';

export interface User {
  id?: number;
  role: Role;
  firstName: string;
  lastName: string;
  email: string;
  password?: string;
  birthdate?: Date;
  gender?: Gender;
}
