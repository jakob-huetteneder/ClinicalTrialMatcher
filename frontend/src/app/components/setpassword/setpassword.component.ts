import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {NgModel} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-setpassword',
  templateUrl: './setpassword.component.html',
  styleUrls: ['./setpassword.component.scss']
})
export class SetpasswordComponent implements OnInit{

  password = '';
  checkPassword = '';
  verificationCode = '';
  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router,
    private toaster: ToastrService
  ) {
  }

  ngOnInit() {
    this.route.params
      .subscribe(params => {
          this.verificationCode = params.code;
          console.log(this.verificationCode);
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

  submit() {

    if (this.password !== this.checkPassword) {
      this.toaster.error('Passwords do not match!');
      return;
    }

    this.userService.setPassword(this.password, this.verificationCode).subscribe({
      next: () => {
        this.toaster.success('Password successfully set!');
        this.router.navigate(['/login']);
      },
      error: error => {
        console.log('Something went wrong while deleting user: ' + error.error.message);
      }
    });
  }
}
