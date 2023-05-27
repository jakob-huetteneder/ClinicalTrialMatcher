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

  constructor(public authService: AuthService) { }

  public get role(): typeof Role {
    return Role;
  }

  ngOnInit() {
  }
}
