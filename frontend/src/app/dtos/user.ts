import {Role} from './role';
import {Gender} from './gender';
import {Status} from './status';


export interface User {
  id?: number;
  firstName: string;
  lastName: string;
  email: string;
  password?: string;
  role?: Role;
  status?: Status;
}

export class UserRegistration implements User {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  birthdate?: Date;
  gender?: Gender;
  role: Role;
  admin?: boolean;
}

export class UserUpdate implements User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  password?: string;
  role?: Role;
  status?: Status;
  oldPassword?: string;
}

export const userToUserUpdate = (user: User) => {
  const userUpdate = new UserUpdate();
  userUpdate.id = user.id;
  userUpdate.firstName = user.firstName;
  userUpdate.lastName = user.lastName;
  userUpdate.email = user.email;
  userUpdate.password = user.password;
  userUpdate.role = user.role;
  userUpdate.status = user.status;
  return userUpdate;
};
