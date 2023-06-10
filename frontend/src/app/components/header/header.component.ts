import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {getRoleString, Role} from '../../dtos/role';
import {Router} from '@angular/router';
import {Trial} from '../../dtos/trial';
import {TrialList} from '../../dtos/trial-list';
import {TrialService} from '../../services/trial.service';
import {TrialListService} from '../../services/trial-list.service';
import {ToastrService} from 'ngx-toastr';
import {FormBuilder, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  trialListForm: FormGroup;
  allLists: TrialList[];

  trialLists: TrialList = {
    id: 1,
    name: 'empty',
    user: {
      id: 1,
      firstName: 'Test',
      lastName: 'Test',
      role: null,
      email: '',
    },
    trials: [],
  };
  collapsed1 = false;

  constructor(
    public authService: AuthService,
    public router: Router,
    private trialListService: TrialListService,
    private notification: ToastrService,
    public formBuilder: FormBuilder
  ) { }

  get roleString(): string {
    return getRoleString(this.authService.getUserRole());
  }

  public get role(): typeof Role {
    return Role;
  }

  ngOnInit() {
    this.trialListForm = this.formBuilder.group({
      id: -1,
      name: [''],
      trials: [],
      user: {
        id: 16,
        firstName: 'Test',
        lastName: 'Test',
        role: null,
        email: '',
      }
    });

      this.reload();
  }

  reload(): void {
    this.trialListService.getOwnTrialLists().subscribe({
      next: data => {
        this.allLists = data;
        console.log('allLists: ', this.allLists);
      }
    });
  }

  logout() {
    this.authService.logoutUser();
    this.router.navigate(['/']);
  }

  createTrialList() {
    console.log('trialListForm: ', this.trialListForm.value);
    this.trialListService.create(this.trialListForm.value).subscribe({
      next: data => {
        this.reload();
        this.notification.success(`Trial List ${data.name} successfully created.`);
      },
      error: error => {
        console.error('error creating Trial List', error);
        this.notification.error(error.error.message, error.error.errors);
      }
    });
  }

}
