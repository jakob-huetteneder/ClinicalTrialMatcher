import {Component, OnInit} from '@angular/core';
import {User} from '../../dtos/user';
import {Status} from '../../dtos/status';
import {Role} from '../../dtos/role';
import {UserService} from '../../services/user.service';
import {NgModel} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-setpassword',
  templateUrl: './setpassword.component.html',
  styleUrls: ['./setpassword.component.scss']
})
export class SetpasswordComponent implements OnInit{
  toRegister: User = {
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    role: undefined,
    status: Status.suspended,
    admin: false
  };
  checkpwd = '';
  checkmail = '';
  verificationCode = '';
  disabled = this.toRegister.firstName === '' || this.toRegister.lastName === ''
    || this.toRegister.email === '' || this.toRegister.password === '' || this.toRegister.role === undefined
    || this.checkmail !== this.toRegister.email || this.checkpwd !== this.toRegister.password
    || (this.toRegister.role === Role.patient && (this.toRegister.gender === undefined || this.toRegister.birthdate === undefined));
  protected readonly role = Role;

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) {
  }

  ngOnInit() {
    this.route.queryParams
      .subscribe(params => {
          this.verificationCode = params.code;
        }
      );
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

  submit() {
    console.log('Create User: ' + this.checkmail);

    const submitted = this.userService.setPassword(this.toRegister, this.verificationCode).subscribe({
      next: () => {
        console.log('Created User: ' + this.toRegister.email);
      },
      error: error => {
        console.log('Something went wrong while deleting user: ' + error.error.message);
      }
    });

    if (submitted) {
      this.router.navigate(['/verification']);
    }
  }
}
