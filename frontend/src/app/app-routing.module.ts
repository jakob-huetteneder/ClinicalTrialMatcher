import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {TrialComponent} from './components/trial/trial.component';
import {CreateTrialComponent} from './components/trial/trial-create/trial-create.component';
import {RegisterComponent} from './components/register/register.component';
import {UserListComponent} from './components/user-list/user-list.component';
import {UpdateProfileComponent} from './components/update-profile/update-profile.component';
import {EditTrialComponent} from './components/trial/trial-edit/trial-edit.component';
import {RegisterPatientComponent} from './components/register/register-patient/register-patient.component';
import {PatientDetailComponent} from './components/patient-detail/patient-detail.component';
import {DiagnoseComponent} from './components/diagnose/diagnose.component';
import {DiagnoseCreateEditMode} from './components/diagnose/diagnose.component';
import {CreateEditExaminationComponent} from './components/examination/create-edit-examination/create-edit-examination.component';
import {ExaminationCreateEditMode} from './components/examination/create-edit-examination/create-edit-examination.component';
import {VerificationComponent} from './components/verification/verification.component';
import {SetpasswordComponent} from './components/setpassword/setpassword.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'update-profile', component: UpdateProfileComponent},
  {path: 'user-overview', component: UserListComponent},
  {path: 'trials', children: [
      {path: '' , component: TrialComponent},
      {path: 'create', component: CreateTrialComponent},
      {path: 'edit/:id', component: EditTrialComponent},
    ]},
  {path: 'user-overview', component: UserListComponent},
  {
    path: 'register', children: [
      {path: '', component: RegisterComponent},
      {path: 'patient', component: RegisterPatientComponent}
    ]
  },
  {path: 'verification', component: VerificationComponent},
  {path: 'password/:code', component: SetpasswordComponent},
  {path: 'patient', children: [
    {path: '', component: PatientDetailComponent},
    {path: ':id', children: [
      {path: '', component: PatientDetailComponent},
      {path: 'examination', children: [
        {path: 'create', component: CreateEditExaminationComponent, data: {mode: ExaminationCreateEditMode.create}},
        {path: 'edit/:eid', component: CreateEditExaminationComponent, data: {mode: ExaminationCreateEditMode.edit}},
      ]},
        {path: 'diagnose', children: [
          {path: 'create', component: DiagnoseComponent, data: {mode: DiagnoseCreateEditMode.create}},
          {path: 'edit/:did', component: DiagnoseComponent, data: {mode: DiagnoseCreateEditMode.edit}},
        ]},
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
