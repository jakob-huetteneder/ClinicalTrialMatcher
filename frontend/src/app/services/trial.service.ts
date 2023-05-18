import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from 'src/environments/environment';
import {Trial} from '../dtos/trial';



const baseUri = environment.backendUrl + '/trials';

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
    return this.http.delete<Trial>(baseUri + '/' + id);
  }


  getById(id: number): Observable<Trial> {
    return this.http.get<Trial>(baseUri + '/' + id);
  }


}
