import {Role} from './role';

export interface User {
  id?: number;
  role: Role;
  fName: string;
  lName: string;
  email: string;
  password?: string;
}
