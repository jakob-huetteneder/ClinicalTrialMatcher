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
import {AutocompleteComponent} from './components/autocomplete/autocomplete.component';
import {PatientDetailComponent} from './components/patient-detail/patient-detail.component';
import {RequestPatientComponent} from './components/doctor-patient-connection/request-patient/request-patient.component';
import {ViewRequestsComponent} from './components/doctor-patient-connection/view-requests/view-requests.component';
import {ViewConnectionsComponent} from './components/doctor-patient-connection/view-connections/view-connections.component';

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
    AutocompleteComponent,
    PatientDetailComponent,
    RequestPatientComponent,
    ViewRequestsComponent,
    ViewConnectionsComponent
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
