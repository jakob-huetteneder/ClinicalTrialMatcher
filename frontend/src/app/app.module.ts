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
import {PatientDetailComponent} from './components/patient/patient-detail/patient-detail.component';
import {
  CreateEditExaminationComponent
} from './components/patient/patient-edit/create-edit-examination/create-edit-examination.component';
import {
  CreateEditDiagnoseComponent
} from './components/patient/patient-edit/create-edit-diagnose/create-edit-diagnose.component';
import {MatDialogModule} from '@angular/material/dialog';
import {ConfirmDialogComponent} from './components/confirm-button/confirm-dialog/confirm-dialog.component';
import {ConfirmButtonComponent} from './components/confirm-button/confirm-button.component';
import {MatButtonModule} from '@angular/material/button';
import {
  RequestPatientComponent
} from './components/doctor-patient-connection/request-patient/request-patient.component';
import {ViewRequestsComponent} from './components/doctor-patient-connection/view-requests/view-requests.component';
import { InteractivefaqComponent } from './components/interactivefaq/interactivefaq.component';
import {
  ViewConnectionsComponent
} from './components/doctor-patient-connection/view-connections/view-connections.component';
import {
  AcceptRegistrationProposalComponent
} from './components/trial-registration/accept-registration-proposal/accept-registration-proposal.component';
import {TrialDetailComponent} from './components/trial/trial-detail/trial-detail.component';
import {StatisticsComponent} from './components/trial/statistics/statistics.component';
import {MatchingPatientComponent} from './components/trial-registration/matching-patient/matching-patient.component';
import {TrialListItemComponent} from './components/trial/trial-list-item/trial-list-item.component';
import {
  AcceptRegistrationRequestsComponent
} from './components/trial-registration/accept-registration-requests/accept-registration-requests.component';
import { PatientEditComponent } from './components/patient/patient-edit/patient-edit.component';
import {SearchComponent} from './components/search/search.component';
import {TrialListComponent} from './components/trial/trial-list/trial-list.component';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import {NgxPaginationModule} from "ngx-pagination";

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
    CreateEditDiagnoseComponent,
    ConfirmDialogComponent,
    ConfirmButtonComponent,
    RequestPatientComponent,
    ViewRequestsComponent,
    ViewConnectionsComponent,
    InteractivefaqComponent,
    MatchingPatientComponent,
    TrialListItemComponent,
    StatisticsComponent,
    AcceptRegistrationProposalComponent,
    TrialDetailComponent,
    AcceptRegistrationRequestsComponent,
    PatientEditComponent,
    SearchComponent,
    TrialListComponent,
    InteractivefaqComponent
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
        MatDialogModule,
        MatButtonModule,
        MatButtonToggleModule,
        NgxPaginationModule
    ],
  providers: [httpInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule {
}
