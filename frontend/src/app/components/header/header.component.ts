import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {Role} from '../../dtos/role';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  collapsed1 = false;

  constructor(
    public authService: AuthService,
    public router: Router,
  ) { }

  get roleString(): string {
    return getRoleString(this.authService.getUserRole());
  }

  public get role(): typeof Role {
    return Role;
  }

  ngOnInit() {
  }

  logout() {
    this.authService.logoutUser();
    this.router.navigate(['/']);
  }

}
