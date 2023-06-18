import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../dtos/user';
import {Faq} from "../dtos/faq";

const baseUri = environment.backendUrl + '/api/v1/faq';

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
   * @param message the user to set password
   * @return observable
   */
  getFaqAnswer(message: string, role: string): Observable<Faq> {
    let queryParams = new HttpParams();
    queryParams = queryParams.set('message', message).set('role', role);
    return this.http.get<Faq>(baseUri, {params: queryParams});
  }

}
