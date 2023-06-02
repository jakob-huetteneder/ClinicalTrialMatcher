import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './components/header/header.component';
import {FooterComponent} from './components/footer/footer.component';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {httpInterceptorProviders} from './interceptors';
import {NgOptimizedImage} from '@angular/common';
import {TrialComponent} from './components/trial/trial.component';
import {CreateEditTrialComponent} from './components/trial/trial-create-edit/trial-create-edit.component';
import {UpdateProfileComponent} from './components/update-profile/update-profile.component';
import {RegisterComponent} from './components/register/register.component';
import {UserListComponent} from './components/user-list/user-list.component';
import {ToastrModule} from 'ngx-toastr';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {DeletePopupComponent} from './components/delete-popup/delete-popup.component';
import {VerificationComponent} from './components/verification/verification.component';
import {SetpasswordComponent} from './components/setpassword/setpassword.component';
import {RegisterPatientComponent} from './components/register/register-patient/register-patient.component';
import {DiseaseAutocompleteComponent} from './components/disease-autocomplete/disease-autocomplete.component';
import {PatientDetailComponent} from './components/patient-detail/patient-detail.component';
import {CreateEditExaminationComponent} from './components/examination/create-edit-examination/create-edit-examination.component';
import {DiagnoseComponent} from './components/diagnose/diagnose.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    TrialComponent,
    CreateEditTrialComponent,
    UpdateProfileComponent,
    RegisterComponent,
    UserListComponent,
    VerificationComponent,
    SetpasswordComponent,
    DeletePopupComponent,
    RegisterPatientComponent,
    PatientDetailComponent,
    DiseaseAutocompleteComponent,
    PatientDetailComponent,
    CreateEditExaminationComponent,
    DiagnoseComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    ToastrModule.forRoot(),
    BrowserAnimationsModule,
    FormsModule,
    NgOptimizedImage,
  ],
  providers: [httpInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule {
}
