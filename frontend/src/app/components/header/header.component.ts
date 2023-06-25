import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {getRoleString, Role} from '../../dtos/role';
import {Router} from '@angular/router';
import {TrialList} from '../../dtos/trial-list';
import {TrialListService} from '../../services/trial-list.service';
import {ToastrService} from 'ngx-toastr';
import {FormBuilder, FormGroup} from '@angular/forms';
import {User} from '../../dtos/user';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  user: User = {
    id: -1,
    firstName: '',
    lastName: '',
    email: ''
  };

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
    trial: [],
  };
  collapsed1 = false;

  constructor(
    public authService: AuthService,
    private userService: UserService,
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
    if (this.authService.isLoggedIn()) {
      this.loadTrialLists();
      this.loadUser();
    }
    this.authService.loginEvent.subscribe(() => {
      this.loadTrialLists();
      this.loadUser();
    });
  }

  loadTrialLists(): void {
    this.trialListService.getOwnTrialLists().subscribe({
      next: data => {
        this.allLists = data;
        console.log('allLists: ', this.allLists);
      }
    });
    this.trialListForm.get('name').setValue('');
  }

  loadUser(): void {
    this.userService.getActiveUser().subscribe({
      next: data => {
        this.user = data;
      },
      error: error => {
        console.error('error loading user', error);
        this.notification.error(error.error.message);
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
        this.loadTrialLists();
        this.trialListService.updateEvent.emit();
        this.notification.success(`Trial List ${data.name} successfully created.`);
      },
      error: error => {
        console.error('error creating Trial List', error);
        this.notification.error(error.error.message, error.error.errors);
      }
    });
  }

  deleteTrialList(trialList: TrialList) {
    console.log('deleteTrialList', trialList);
    this.trialListService.deleteTrialList(trialList).subscribe({
      next: data => {
        this.trialListService.updateEvent.emit();
        this.loadTrialLists();
        this.notification.success(`Trial List ${trialList.name} successfully deleted.`);
      },
      error: error => {
        console.error('error deleting Trial List', error);
        this.notification.error(error.error.message, error.error.errors);
      }
    });
  }
}
