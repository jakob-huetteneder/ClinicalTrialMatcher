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
import {CreateTrialComponent} from './components/trial/trial-create/trial-create.component';
import {RegisterComponent} from './components/register/register.component';
import {UpdateProfileComponent} from './components/update-profile/update-profile.component';
import {UserListComponent} from './components/user-list/user-list.component';
import {RegisterPatientComponent} from './components/register/register-patient/register-patient.component';
import {AutocompleteComponent} from './components/autocomplete/autocomplete.component';
import {PatientDetailComponent} from './components/patient-detail/patient-detail.component';
import {ToastrModule} from 'ngx-toastr';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {DeletePopupComponent} from './components/delete-popup/delete-popup.component';
import {EditTrialComponent} from './components/trial/trial-edit/trial-edit.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    TrialComponent,
    CreateTrialComponent,
    UpdateProfileComponent,
    EditTrialComponent,
    RegisterComponent,
    UserListComponent,
    DeletePopupComponent,
    RegisterPatientComponent,
    AutocompleteComponent,
    PatientDetailComponent,
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
