import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {RegisterComponent} from './components/register/register.component';
import {UserListComponent} from './components/user-list/user-list.component';
import {RegisterPatientComponent} from './components/register/register-patient/register-patient.component';
import {PatientDetailComponent} from './components/patient-detail/patient-detail.component';
import {RequestPatientComponent} from './components/doctor-patient-connection/request-patient/request-patient.component';
import {ViewRequestsComponent} from './components/doctor-patient-connection/view-requests/view-requests.component';
import {Role} from './dtos/role';
import {ViewConnectionsComponent} from './components/doctor-patient-connection/view-connections/view-connections.component';
import {AuthGuard} from './guards/auth.guard';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {
    path: 'register', children: [
      {path: '', component: RegisterComponent},
      {path: 'patient', component: RegisterPatientComponent}
    ]
  },
  {path: 'admin',
    data: {
      allowedRoles: [Role.admin]
    },
    canActivateChild: [AuthGuard],
    children: [
      {path: 'user-overview', component: UserListComponent},
  ]},
  {path: 'patient',
    data: {
      allowedRoles: [Role.patient]
    },
    canActivateChild: [AuthGuard],
    children: [
      {path: 'requests', component: ViewRequestsComponent},
      {path: ':id', component: PatientDetailComponent}, // must be last, otherwise the path 'requests' will be interpreted as an id
  ]},
  {path: 'doctor',
    data: {
      allowedRoles: [Role.doctor]
    },
    canActivateChild: [AuthGuard],
    children: [
      {path: 'request-patient', component: RequestPatientComponent},
      {path: 'my-patients', children: [
          {path: '', component: ViewConnectionsComponent},
          {path: ':id', component: PatientDetailComponent}, // must be last, otherwise the path 'requests' will be interpreted as an id
      ]},
  ]},
  {path: '**', redirectTo: ''},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
