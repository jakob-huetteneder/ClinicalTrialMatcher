import {Component, OnInit} from '@angular/core';
import {User, UserRegistration} from '../../dtos/user';
import {UserService} from '../../services/user.service';
import {cloneDeep} from 'lodash';
import {Role} from '../../dtos/role';
import {Status} from '../../dtos/status';
import {ToastrService} from 'ngx-toastr';
import {Router} from '@angular/router';


@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit {

  users: User[] = [];
  editedUsers: User[] = []; // stores the old values of currently edited users

  registeringUser = false;
  toRegister = new UserRegistration();

  constructor(
    private userService: UserService,
    private notification: ToastrService,
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

        this.notification.info('Successfully deleted user ' + user.email);
      },
      error: error => {
        console.log('Something went wrong while deleting user: ' + error.error.message);
        this.notification.error(error.error.message, 'Error deleting user');
      }
    });
  }

  saveUser() {
    console.log('Create User: ' + this.toRegister.email);

    this.toRegister.password = 'no password';

    this.userService.createUser(this.toRegister).subscribe({
      next: createdUser => {
        this.notification.info('Successfully created user ' + createdUser.email);
        this.users.push(createdUser);
        this.registeringUser = false;
        this.toRegister = new UserRegistration();
      },
      error: error => {
        console.log('Something went wrong while creating user: ' + error.error.message);
        console.log(error);
        if (error.status === 409) {
          this.notification.error('User with email ' + this.toRegister.email + ' already exists');
        } else if (error.status === 422) {
          let listOfValidationErrors = '';
          error.error.errors.forEach((validationError: string) => {
            if (listOfValidationErrors !== '') {
              listOfValidationErrors += ', ';
            }
            listOfValidationErrors += validationError;
          });
          this.notification.error(listOfValidationErrors, 'Invalid values');
        } else {
          this.notification.error(error.error.message, 'Error creating user');
        }
      }
    });
  }


  confirmEditUser(user: User) {
    console.log('Update user: ' + user.email);
    this.userService.updateUserById(user).subscribe({
      next: updatedUser => {
        console.log('Updated user: ' + updatedUser.email);

        // remove user from editedUsers
        this.editedUsers = this.editedUsers.filter(editedUser => editedUser.id !== user.id);
        this.notification.info('Successfully updated user ' + updatedUser.email);
      },
      error: error => {
        if (error.status === 409) {
          this.notification.error('User with email ' + user.email + ' already exists');
        } else if (error.status === 422) {
          let listOfValidationErrors = '';
          error.error.errors.forEach((validationError: string) => {
            if (listOfValidationErrors !== '') {
              listOfValidationErrors += ', ';
            }
            listOfValidationErrors += validationError;
          });
          this.notification.error(listOfValidationErrors, 'Invalid values');
        } else {
          this.notification.error(error.error.message, 'Error updating user');
        }
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
    this.registeringUser = false;
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
    this.registeringUser = true;
  }

  onAdd(): boolean {
    return this.registeringUser;
  }

  roleName(role: Role): string {
    return role.charAt(0).toUpperCase() + role.slice(1).toLowerCase();
  }

  statusName(status: Status): string {
    return status.charAt(0).toUpperCase() + status.slice(1).toLowerCase().replace('_', ' ');
  }

  private loadUsers() {
    this.userService.getAllUsers().subscribe({
      next: (users: User[]) => {
        this.users = users;
      },
      error: error => {
        console.log('Something went wrong while loading users: ' + error.error.message);
        this.notification.error(error.error.message, 'Something went wrong while loading users');
        console.log(error);
      }
    });
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
