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
import {RegisterComponent} from './components/register/register.component';
import {UserListComponent} from './components/user-list/user-list.component';
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
    RegisterComponent,
    UserListComponent,
    RegisterPatientComponent,
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
    FormsModule,
    NgOptimizedImage,
  ],
  providers: [httpInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule {
}
