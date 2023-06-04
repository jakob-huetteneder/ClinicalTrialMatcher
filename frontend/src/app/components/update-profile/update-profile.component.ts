import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators} from '@angular/forms';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';
import {UserService} from '../../services/user.service';
import {User, userToUserUpdate, UserUpdate} from '../../dtos/user';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-update-profile',
  templateUrl: './update-profile.component.html',
  styleUrls: ['./update-profile.component.scss']
})
export class UpdateProfileComponent implements OnInit {
  user = new UserUpdate();
  oldUser = new UserUpdate();

  editForm: FormGroup;


  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private userService: UserService,
    private notification: ToastrService
  ) {
  }

  ngOnInit(): void {
    this.editForm = this.formBuilder.group({
      firstName: ['', [Validators.required, Validators.maxLength(255)]],
      lastName: ['', [Validators.required, Validators.maxLength(255)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.maxLength(255), Validators.minLength(8)]],
      repeatMail: ['', [Validators.email]],
      repeatPassword: ['', [Validators.maxLength(255), Validators.minLength(8)]],
      oldPassword: ['', [Validators.maxLength(255), Validators.minLength(8)]],
    });
    this.userService.getActiveUser()
      .subscribe({
        next: data => {
          this.user = userToUserUpdate(data);
          this.editForm.addValidators(this.changed(this.user));
          this.editForm.addValidators(this.passwordChanged());
          this.editForm.patchValue({
            firstName: this.user.firstName,
            lastName: this.user.lastName,
            email: this.user.email
          });
          console.log('data', data);
        },
        error: error => {
          console.error('Error fetching trials', error);
        }
      });
  }

  public update(): void {
    if (!this.editForm.valid) {

      this.notification.error(this.getErrorString());
      return;
    }

    this.user.firstName = this.editForm.value.firstName;
    this.user.lastName = this.editForm.value.lastName;
    this.user.email = this.editForm.value.email;
    this.user.password = this.editForm.value.password;
    this.user.oldPassword = this.editForm.value.oldPassword;


    if (this.editForm.value.password === '') {
      this.user.password = null;
    }
    if (this.editForm.value.repeatPassword === '') {
      this.user.oldPassword = null;
    }

    this.userService.updateUser(this.user).subscribe({
      next: () => {
        this.router.navigate(['']);
        this.notification.success('Profile updated successfully');
      },
      error: error => {
        console.error('Error ', error.error.message);
        if (error.status === 409) {
          this.notification.error('Email already exists, please choose another one');
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
          this.notification.error(error.error.message);
        }
      }
    });
  }

  delete(id: number) {
    console.log('delete user with name ' + this.user.firstName + ' ' + this.user.lastName + ' and id ' + id);
    return this.userService.deleteUser(id).subscribe({
      next: () => {
        this.authService.logoutUser();
        this.router.navigate(['']);
      },
      error: error => {
        console.error('Error ', error.error.message);
        this.notification.error(error.error.message, 'Error while deleting');
      }
    });
  }

  cancel() {
    this.router.navigate(['']);
  }

  // Custom validators
  changed(oldValue: User): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const firstNameChanged = oldValue.firstName !== control.value.firstName;
      const lastNameChanged = oldValue.lastName !== control.value.lastName;
      const emailChanged = oldValue.email !== control.value.email;
      const emailMatches = control.value.email === control.value.repeatMail;

      if (emailChanged && !emailMatches) {
        return { emailMismatch: true };
      }
      return firstNameChanged || lastNameChanged || emailChanged ? null : { noUpdateRequired: true };
    };
  }

  passwordChanged(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (control.value.password !== control.value.repeatPassword) {
        return { passwordMismatch: true };
      }
      if (control.value.password !== '' && control.value.oldPassword === '') {
        return { oldPasswordRequired: true };
      }
      return null;
    };
  }

  getErrorString() {

    const errors = [];
    if (this.editForm.errors != null) {
      Object.keys(this.editForm.errors).forEach(keyError => {
        console.log('Key error: ' + keyError);
        errors.push(this.validationErrorName(keyError));
      });
    }
    Object.keys(this.editForm.controls).forEach(key => {
      const controlErrors: ValidationErrors = this.editForm.get(key).errors;
      if (controlErrors != null) {
        Object.keys(controlErrors).forEach(keyError => {
          console.log('Key control: ' + key + ', keyError: ' + keyError);
          errors.push(this.fieldName(key) + ' ' + this.validationErrorName(keyError));
        });
      }
    });

    return errors.pop();
  }

  fieldName(field: string): string {
    switch (field) {
      case 'firstName':
        return 'First name';
      case 'lastName':
        return 'Last name';
      case 'email':
        return 'Email';
      case 'password':
        return 'Password';
      case 'repeatMail':
        return 'Repeat email';
      case 'repeatPassword':
        return 'Repeat password';
      case 'oldPassword':
        return 'Old password';
    }
    return 'Invalid input';
  }

  validationErrorName(error: string): string {
    switch (error) {
      case 'required':
        return 'is required';
      case 'email':
        return 'is not a valid email';
      case 'minlength':
        return 'is too short';
      case 'maxlength':
        return 'is too long';
      case 'noUpdateRequired':
        return 'No changes made';
      case 'emailMismatch':
        return 'Emails do not match';
      case 'passwordMismatch':
        return 'Passwords do not match';
      case 'oldPasswordRequired':
        return 'Old password required';
    }
    return 'Invalid input';
  }
}
