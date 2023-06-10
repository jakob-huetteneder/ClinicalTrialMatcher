import {environment} from '../../environments/environment';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Trial} from '../dtos/trial';
import {TrialList} from '../dtos/trial-list';
import {List} from 'postcss/lib/list';

const baseUri = environment.backendUrl + '/api/v1/trialList';

@Injectable({
  providedIn: 'root'
})
export class TrialListService {

  constructor(
    private http: HttpClient,
  ) {
  }

  /**
   * Get the trial lists belonging to the logged-in user
   *
   * @return observable list of found trial lists.
   */
  getOwnTrialLists(): Observable<TrialList[]> {
    return this.http.get<TrialList[]>(baseUri);
  }

  /**
   * Create a new trialList in the system.
   *
   * @param trialList the data for the trialList that should be created
   * @return an Observable for the created trialList
   */
  create(trialList: TrialList): Observable<TrialList> {
    return this.http.post<TrialList>(
      baseUri,
      trialList
    );
  }

}
