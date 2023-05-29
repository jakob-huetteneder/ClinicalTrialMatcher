import {Component, OnInit} from '@angular/core';
import {User} from '../../dtos/user';
import {UserService} from '../../services/user.service';
import {cloneDeep} from 'lodash';
import {Role} from '../../dtos/role';
import {Status} from '../../dtos/status';


@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit {

  users: User[] = [];
  editedUsers: User[] = []; // stores the old values of currently edited users
  addingUser = false;

  toRegister: User = {
    firstName: '',
    lastName: '',
    email: '',
    password: 'password',
    gender: undefined,
    birthdate: undefined,
    role: undefined,
    status: Status.suspended,
    admin: true
  };
  checkmail = '';

  constructor(
    private userService: UserService
  ) {
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  editUser(user: User) {
    this.editedUsers.push(cloneDeep(user));
  }

  deleteUser(user: User) {
    console.log('Delete user: ' + user.email);

    this.userService.deleteUser(user.id).subscribe({
      next: () => {
        console.log('Deleted user: ' + user.email);

        // remove user from users
        this.users = this.users.filter(u => u.id !== user.id);
      },
      error: error => {
        console.log('Something went wrong while deleting user: ' + error.error.message);
      }
    });
  }

  saveUser() {
    console.log('Create User: ' + this.checkmail);

    this.userService.createUser(this.toRegister).subscribe({
      next: () => {
        console.log('Created User: ' + this.toRegister.email);
      },
      error: error => {
        console.log('Something went wrong while creating user: ' + error.error.message);
      }
    });

    window.location.reload();
  }


  confirmEditUser(user: User) {
    console.log('Update user: ' + user.email);
    const updatedUserPromise = this.userService.updateUserById(user);
    updatedUserPromise.subscribe({
      next: updatedUser => {
        console.log('Updated user: ' + updatedUser.email);

        // remove user from editedUsers
        this.editedUsers = this.editedUsers.filter(editedUser => editedUser.id !== user.id);
      },
      error: error => {
        // TODO: check if error is a validation error
        console.log('Something went wrong while updating user: ' + error.error.message);
        console.log('The following values were invalid:\n' + JSON.stringify(error.error.errors));
        console.log(error);
        // reset user to old values
        this.resetUser(user.id);
      }
    });
  }

  discardEditUser(user: User) {
    console.log('Discard user: ' + user.email);

    this.resetUser(user.id);
  }

  discardAddUser() {
    console.log('Discard user: ');
    this.addingUser = false;
  }

  activateUser(user: User) {
    console.log('Activate user: ' + user.email);
    user.status = Status.active;

    this.confirmEditUser(user);
  }

  isEdited(user: User): boolean {
    return this.editedUsers.some(editedUser => editedUser.id === user.id);
  }

  addUser() {
    this.addingUser = true;
  }

  onAdd(): boolean {
    return this.addingUser;
  }

  roleName(role: Role): string {
    return role.charAt(0).toUpperCase() + role.slice(1).toLowerCase();
  }

  statusName(status: Status): string {
    return status.charAt(0).toUpperCase() + status.slice(1).toLowerCase().replace('_', ' ');
  }

  private loadUsers() {
    this.userService.getAllUsers().subscribe(
      (users: User[]) => {
        this.users = users;
      },
      error => {
        console.log('Something went wrong while loading users: ' + error.error.message);
        console.log(error);
      }
    );
  }

  private resetUser(userId: number) {
    const oldUser = this.editedUsers.find(editedUser => editedUser.id === userId);
    if (oldUser) {
      // change user in users
      const index = this.users.findIndex(user => user.id === userId);
      this.users[index] = oldUser;
    }
    // remove user from editedUsers
    this.editedUsers = this.editedUsers.filter(editedUser => editedUser.id !== userId);
  }
}
