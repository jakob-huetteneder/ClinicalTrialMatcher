import {Component, OnInit} from '@angular/core';
import {NgModel, UntypedFormBuilder, UntypedFormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../services/auth.service';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../../services/user.service';
import {User} from '../../dtos/user';

@Component({
  selector: 'app-update-profile',
  templateUrl: './update-profile.component.html',
  styleUrls: ['./update-profile.component.scss']
})
export class UpdateProfileComponent implements OnInit {
  updateForm: UntypedFormGroup;
  submitted = false;
  user: User = {
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    oldPassword: '',
};
  checkmail = '';
  checkpwd = '';
  disabled = this.user.firstName === '' || this.user.lastName === ''
    || this.user.email === '' || this.user.password === ''|| this.checkmail !== this.user.email
    || this.checkpwd !== this.user.password;
constructor(private formBuilder: UntypedFormBuilder,
            private authService: AuthService,
            private router: Router,
            private userService: UserService,
            private route: ActivatedRoute) {
      this.updateForm = this.formBuilder.group({
      oldPassword: ['', [Validators.required]]
    });
  }

  public dynamicCssClassesForInput(input: NgModel): any {
    return {
      // This names in this object are determined by the style library,
      // requiring it to follow TypeScript naming conventions does not make sense.
      // eslint-disable-next-line @typescript-eslint/naming-convention
      'is-invalid': !input.valid && !input.pristine,
    };
  }

  ngOnInit(): void {
      this.route.data.subscribe(() => {
        this.route.params.subscribe(params => this.user.id = params.id);
      });
      let userId = 0;
      this.route.params.subscribe(data => {
        userId = data.id;
      });
      this.userService.getById(userId).subscribe(data => {
        this.user = data;
      });
  }

  public onSubmit(): void {
      const observable = this.userService.updateUser(this.user);

      observable.subscribe({
        next: () => {
          this.router.navigate(['/']);
        },
        error: error => {
          console.error('Error ', error.error.message);
        }
      });
  }

  delete(id) {
    return this.userService.deleteUser(id).subscribe({
      next: () => {
        this.router.navigate(['']);
      },
      error: error => {
        console.error('Error ', error.error.message);
      }
    });
  }

  openForm() {
    document.getElementById('myForm').style.display = 'flex';
  }

  closeForm() {
    document.getElementById('myForm').style.display = 'none';
  }

}
