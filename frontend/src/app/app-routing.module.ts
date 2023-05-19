import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {RegisterComponent} from './components/register/register.component';
import {UserListComponent} from './components/user-list/user-list.component';
import {RegisterPatientComponent} from './components/register/register-patient/register-patient.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'user-overview', component: UserListComponent},
  {
    path: 'register', children: [
      {path: '', component: RegisterComponent},
      {path: 'patient', component: RegisterPatientComponent}
    ]
  },
  {path: '**', redirectTo: ''},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
