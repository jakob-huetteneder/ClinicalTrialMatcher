import {environment} from '../../environments/environment';
import {EventEmitter, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Trial} from '../dtos/trial';
import {TrialList} from '../dtos/trial-list';


const baseUri = environment.backendUrl + '/api/v1/trialList';

@Injectable({
  providedIn: 'root'
})
export class TrialListService {
  updateEvent = new EventEmitter<number>();

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

  addTrialToList(trial: Trial, list: TrialList): Observable<TrialList> {
    return this.http.put<TrialList>(
      baseUri + '/' + list.id,
      trial
    );
  }

  deleteTrialList(list: TrialList): Observable<TrialList> {
    return this.http.delete<TrialList>(
      baseUri + '/' + list.id
    );
  }

  getTrialListById(id: number): Observable<TrialList> {
    return this.http.get<TrialList>(
      baseUri + '/' + id
    );
  }

  // delete trial from trial list with update:
  deleteTrialFromList(trialId: number, list: TrialList): Observable<TrialList> {
    return this.http.put<TrialList>(
      baseUri + '/' + trialId + '/' + list.id, list);
  }
}
