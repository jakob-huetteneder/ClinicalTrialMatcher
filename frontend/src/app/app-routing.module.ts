import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {RegisterComponent} from './components/register/register.component';
import {UserListComponent} from './components/user-list/user-list.component';
import {VerificationComponent} from './components/verification/verification.component';
import {SetpasswordComponent} from './components/setpassword/setpassword.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'user-overview', component: UserListComponent},
  {
    path: 'register', children: [
      {path: '', component: RegisterComponent},
      {path: 'patient', component: RegisterComponent}
    ]
  },
  {path: 'verification', component: VerificationComponent},
  {path: 'password/:code', component: SetpasswordComponent},
  {path: '**', redirectTo: ''},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
