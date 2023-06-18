import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {TrialComponent} from './components/trial/trial.component';
import {
  CreateEditTrialComponent,
  TrialCreateEditMode
} from './components/trial/trial-create-edit/trial-create-edit.component';
import {RegisterComponent} from './components/register/register.component';
import {UserListComponent} from './components/user-list/user-list.component';
import {UpdateProfileComponent} from './components/update-profile/update-profile.component';
import {RegisterPatientComponent} from './components/register/register-patient/register-patient.component';
import {PatientDetailComponent} from './components/patient/patient-detail/patient-detail.component';
import {CreateEditDiagnoseComponent} from './components/patient/patient-edit/create-edit-diagnose/create-edit-diagnose.component';
import {DiagnoseCreateEditMode} from './components/patient/patient-edit/create-edit-diagnose/create-edit-diagnose.component';
import {
  CreateEditExaminationComponent
} from './components/patient/patient-edit/create-edit-examination/create-edit-examination.component';
import {ExaminationCreateEditMode} from './components/patient/patient-edit/create-edit-examination/create-edit-examination.component';
import {VerificationComponent} from './components/verification/verification.component';
import {SetpasswordComponent} from './components/setpassword/setpassword.component';
import {Role} from './dtos/role';
import {AuthGuard} from './guards/auth.guard';
import {
  RequestPatientComponent
} from './components/doctor-patient-connection/request-patient/request-patient.component';
import {
  ViewConnectionsComponent
} from './components/doctor-patient-connection/view-connections/view-connections.component';
import {ViewRequestsComponent} from './components/doctor-patient-connection/view-requests/view-requests.component';
import {StatisticsComponent} from './components/trial/statistics/statistics.component';
import {
  AcceptRegistrationProposalComponent
} from './components/trial-registration/accept-registration-proposal/accept-registration-proposal.component';
import {TrialDetailComponent} from './components/trial/trial-detail/trial-detail.component';
import {MatchingPatientComponent} from './components/trial-registration/matching-patient/matching-patient.component';
import {
  AcceptRegistrationRequestsComponent
} from './components/trial-registration/accept-registration-requests/accept-registration-requests.component';
import {PatientEditComponent} from './components/patient/patient-edit/patient-edit.component';
import {SearchComponent} from './components/search/search.component';
import {TrialListComponent} from './components/trial/trial-list/trial-list.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'trial/detail/:id', component: TrialDetailComponent},
  {path: 'search', component: SearchComponent},
  {path: 'trial-list/:id', component: TrialListComponent},
  {path: 'account', children: [
      {path: '', component: UpdateProfileComponent},
      {path: 'login', component: LoginComponent},
      {path: 'register', component: RegisterComponent},
      {path: 'verified', component: VerificationComponent},
      {path: 'set-password/:code', component: SetpasswordComponent},
    ]},
  {path: 'admin', data: {allowedRoles: [Role.admin]},
    canActivate: [AuthGuard],
    canActivateChild: [AuthGuard],
    children: [
      {path: 'user-overview', component: UserListComponent},
    ]},
  {path: 'researcher', data: {allowedRoles: [Role.researcher]},
    canActivate: [AuthGuard],
    canActivateChild: [AuthGuard],
    children: [
      {path: 'trials', children: [
          {path: '' , component: TrialComponent},
          {path: 'create', component: CreateEditTrialComponent, data: {mode: TrialCreateEditMode.create}},
          {path: 'edit/:id', component: CreateEditTrialComponent, data: {mode: TrialCreateEditMode.edit}},
          {path: 'requests/:trialId', children: [
              {path: '', component: AcceptRegistrationRequestsComponent},
              {path: 'patient/:id', component: PatientDetailComponent},
            ]},
        ]},
    ]},
  {path: 'doctor', data: {allowedRoles: [Role.doctor]},
    canActivate: [AuthGuard],
    canActivateChild: [AuthGuard],
    children: [
      {path: 'register-patient', component: RegisterPatientComponent},
      {path: 'request-patient', component: RequestPatientComponent},
      {path: 'my-patients', component: ViewConnectionsComponent, data: {role: Role.doctor}},
      {path: 'edit-patient/:id', children: [
          {path: '', component: PatientEditComponent},
          {path: 'examination', children: [
              {path: 'create', component: CreateEditExaminationComponent, data: {mode: ExaminationCreateEditMode.create}},
              {path: 'edit/:eid', component: CreateEditExaminationComponent, data: {mode: ExaminationCreateEditMode.edit}},
              ]},
          {path: 'diagnose', children: [
              {path: 'create', component: CreateEditDiagnoseComponent, data: {mode: DiagnoseCreateEditMode.create}},
              {path: 'edit/:did', component: CreateEditDiagnoseComponent, data: {mode: DiagnoseCreateEditMode.edit}},
              ]},
          ]},
      {path: 'view-patient/:id', component: PatientDetailComponent},
      {path: 'trial/:trialId/match-patient', component: MatchingPatientComponent},

    ]},
  {path: 'patient', data: {allowedRoles: [Role.patient]},
    canActivate: [AuthGuard],
    canActivateChild: [AuthGuard],
    children: [
      {path: 'requests', component: ViewRequestsComponent},
      {path: 'connections', component: ViewConnectionsComponent, data: {role: Role.patient}},
      {path: 'trial-registrations', component: AcceptRegistrationProposalComponent},
    ]},
  {path: '**', redirectTo: ''},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
