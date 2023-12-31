import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from 'src/environments/environment';
import {Filter} from '../dtos/filter';
import {Trial, TrialRegistration} from '../dtos/trial';
import {Patient} from '../dtos/patient';


const baseUri = environment.backendUrl + '/api/v1/trials';

@Injectable({
  providedIn: 'root'
})
export class TrialService {

  constructor(
    private http: HttpClient,
  ) {
  }

  /**
   * Get all trials stored in the system
   *
   * @return observable list of found trials.
   */
  getAll(): Observable<Trial[]> {
    return this.http.get<Trial[]>(baseUri);
  }

  /**
   * Search for trials stored in the system
   *
   * @return observable list of found trials.
   */
  searchForTrial(keyword: string): Observable<Trial[]> {
    let queryParams = new HttpParams();
    queryParams = queryParams.set('keyword', keyword);
    return this.http.get<Trial[]>(baseUri + '/search', {params: queryParams});
  }

  /**
   * Search for trials stored in the system with filter
   *
   * @return observable list of found trials.
   */
  searchForTrialWithFilter(keyword: string, filter: Filter): Observable<any> {
    let queryParams = new HttpParams();
    queryParams = queryParams.set('keyword', keyword);
    return this.http.post<any>(baseUri + '/search', filter, {params: queryParams});
  }


  /**
   * Get the trials belonging to the researcher stored in the system
   *
   * @return observable list of found trials.
   */
  getResearcherTrials(): Observable<Trial[]> {
    return this.http.get<Trial[]>(baseUri + '/researcher');
  }

  getAllMatchingPatients(trialId: number): Observable<Patient[]> {
    return this.http.get<Patient[]>(baseUri + '/match/' + trialId);
  }


  /**
   * Create a new trial in the system.
   *
   * @param trial the data for the trial that should be created
   * @return an Observable for the created trial
   */
  create(trial: Trial): Observable<Trial> {
    return this.http.post<Trial>(
      baseUri,
      trial
    );
  }

  edit(trial: Trial): Observable<Trial> {
    return this.http.put<Trial>(
      baseUri + '/' + trial.id,
      trial
    );
  }


  deleteTrial(id: number | undefined): Observable<Trial> {
    console.log(id);
    return this.http.delete<Trial>(baseUri + '/' + id);
  }


  getById(id: number): Observable<Trial> {
    return this.http.get<Trial>(baseUri + '/' + id);
  }

  registerAsUser(trialId: number): Observable<void> {
    return this.http.post<void>(baseUri + '/registration/' + trialId, undefined);
  }

  registerAsDoctor(trialId: number, patientId: number): Observable<void> {
    return this.http.post<void>(baseUri + '/registration/' + trialId + '/patient/' + patientId, undefined);
  }

  checkIfAlreadyApplied(trialId: number): Observable<TrialRegistration> {
    return this.http.get<TrialRegistration>(baseUri + '/registration/patient/' + trialId);
  }

  getAllRegistrationsForTrial(trialId: number): Observable<TrialRegistration[]> {
    return this.http.get<TrialRegistration[]>(baseUri + '/registration/' + trialId);
  }

  getAllRegistrationsForLoggedInPatient(): Observable<TrialRegistration[]> {
    return this.http.get<TrialRegistration[]>(baseUri + '/registration');
  }

  respondToRegistrationProposal(trialId: number, accept: boolean): Observable<TrialRegistration> {
    return this.http.put<TrialRegistration>(baseUri + '/registration/' + trialId + '/response', accept);
  }

  respondToRegistrationRequest(trialId: number, patientId: number, accept: boolean): Observable<TrialRegistration> {
    return this.http.put<TrialRegistration>(baseUri + '/registration/' + trialId + '/patient/' + patientId + '/response', accept);
  }
}
