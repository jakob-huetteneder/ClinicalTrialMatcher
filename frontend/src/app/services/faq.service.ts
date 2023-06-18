import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../dtos/user';
import {environment} from '../../environments/environment';

const baseUri = environment.backendUrl + '/api/v1/users';

@Injectable({
  providedIn: 'root'
})
export class FaqService {
  constructor(
    private http: HttpClient,
  ) {
  }

  /**
   * Set the password for a new user.
   *
   * @param keyword the user to set password
   * @return observable
   */
  getFaqAwnser(keyword: string): Observable<void> {
    let queryParams = new HttpParams();
    queryParams = queryParams.set('keyword', keyword);
    return this.http.get<void>(baseUri + '/password', {params: queryParams});
  }

}
