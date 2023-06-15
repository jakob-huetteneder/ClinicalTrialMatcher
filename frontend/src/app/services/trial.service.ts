import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from 'src/environments/environment';
import {Trial} from '../dtos/trial';



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
   * Get the trials belonging to the researcher stored in the system
   *
   * @return observable list of found trials.
   */
  getResearcherTrials(): Observable<Trial[]> {
    return this.http.get<Trial[]>(baseUri + '/researcher');
  }


  /**
   * Create a new trial in the system.
   *
   * @param trial the data for the trial that should be created
   * @return an Observable for the created trial
   */
  create(trial: Trial): Observable<Trial> {
    console.log('create: ', trial);
    return this.http.post<Trial>(
      baseUri,
      trial
    );
  }

  edit(trial: Trial): Observable<Trial> {
    console.log('edit: ', trial);
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
}
