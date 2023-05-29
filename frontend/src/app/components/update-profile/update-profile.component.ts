import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, NgModel, UntypedFormBuilder, Validators} from '@angular/forms';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';
import {UserService} from '../../services/user.service';
import {User} from '../../dtos/user';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-update-profile',
  templateUrl: './update-profile.component.html',
  styleUrls: ['./update-profile.component.scss']
})
export class UpdateProfileComponent implements OnInit {
  user: User = {
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    oldPassword: '',
  };
  checkmail = '';
  checkpwd = '';
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
      password: ['', [Validators.required, Validators.maxLength(255), Validators.minLength(8)]],
      repeatMail: ['', [Validators.required, Validators.email]],
      repeatPassword: ['', [Validators.required, Validators.maxLength(255), Validators.minLength(8)]],
      oldPassword: ['', [Validators.required, Validators.maxLength(255), Validators.minLength(8)]],
    });
    this.userService.getActiveUser()
      .subscribe({
        next: data => {
          this.user = data;
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

  public onSubmit(): void {
    console.log(this.editForm.value);
    this.user.firstName = this.editForm.value.firstName;
    this.user.lastName = this.editForm.value.lastName;
    this.user.email = this.editForm.value.email;
    this.user.password = this.editForm.value.password;
    this.user.oldPassword = this.editForm.value.oldPassword;
    console.log(this.user);
    this.userService.updateUser(this.user).subscribe({
      next: () => {
        this.router.navigate(['']);
        this.notification.success('Profile updated successfully');
      },
      error: error => {
        console.error('Error ', error.error.message);
      }
    });
  }

  delete(id: number) {
    return this.userService.deleteUser(id).subscribe({
      next: () => {
        this.authService.logoutUser();
        this.router.navigate(['']);
      },
      error: error => {
        console.error('Error ', error.error.message);
      }
    });
  }


}
