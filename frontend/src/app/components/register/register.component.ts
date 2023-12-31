import {Component} from '@angular/core';
import {User, UserRegistration} from 'src/app/dtos/user';
import {Role} from '../../dtos/role';
import {NgModel} from '@angular/forms';
import {UserService} from '../../services/user.service';
import {Status} from '../../dtos/status';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})

export class RegisterComponent {
  toRegister: UserRegistration = new UserRegistration();
  submitted = false;
  checkpwd = '';
  checkmail = '';
  disabled = this.toRegister.firstName === '' || this.toRegister.lastName === '' || this.checkpwd.length < 8
    || this.toRegister.email === '' || this.toRegister.password === '' || this.toRegister.role === undefined
    || this.checkmail !== this.toRegister.email || this.checkpwd !== this.toRegister.password
    || (this.toRegister.role === Role.patient && (this.toRegister.gender === undefined || this.toRegister.birthdate === undefined));
  protected readonly role = Role;
  constructor(
    private userService: UserService,
    private router: Router,
    private notification: ToastrService
  ) {
  }
  public dynamicCssClassesForInput(input: NgModel): any {
    return {
      // This names in this object are determined by the style library,
      // requiring it to follow TypeScript naming conventions does not make sense.
      // eslint-disable-next-line @typescript-eslint/naming-convention
      'is-invalid': !input.valid && !input.pristine,
    };
  }
  public tmp(): void {
    console.log(this.toRegister);
  }
  public buttonstyle(): string {
    this.disabled = this.toRegister.firstName === '' || this.toRegister.lastName === '' || this.checkpwd.length < 8
      || this.toRegister.email === '' || this.toRegister.password === '' || this.toRegister.role === undefined
      || this.checkmail !== this.toRegister.email || this.checkpwd !== this.toRegister.password
      || (this.toRegister.role === Role.patient && (this.toRegister.gender === undefined || this.toRegister.birthdate === undefined));

    if (this.disabled) {
      return 'bg-gray-400 hover:cursor-not-allowed';
    } else {
      return 'transition ease-in-out delay-100 duration-300 bg-blue-500 hover:-translate-y-0 hover:scale-110 hover:bg-blue-400';
    }
  }
  submit() {
    console.log('Create User: ' + this.checkmail);
    this.submitted = true;
    this.disabled = true;
    this.checkmail = '';
    this.checkpwd = '';

    this.userService.createUser(this.toRegister).subscribe({
      next: () => {
        console.log('Created User: ' + this.toRegister.email);
        this.notification.info('Please check your e-mail', 'Registration was successful');
        this.router.navigate(['']).then();
      },
      error: error => {
        console.log('Something went wrong while creating user: ' + error.error.message);
        this.notification.error(error.error.message, 'Registration failed');
      }
    });
  }
}
