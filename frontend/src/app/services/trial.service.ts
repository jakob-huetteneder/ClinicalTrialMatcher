import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from 'src/environments/environment';
import {Trial} from '../dtos/trial';
import {Filter} from '../dtos/filter';



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
  searchForTrialWithFilter(keyword: string, filter: Filter): Observable<Trial[]> {
    let queryParams = new HttpParams();
    queryParams = queryParams.set('keyword', keyword);
    return this.http.post<Trial[]>(baseUri + '/search', filter, {params: queryParams});
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


}
