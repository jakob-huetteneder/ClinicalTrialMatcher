import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Faq} from '../dtos/faq';
import {environment} from '../../environments/environment';

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
   * Get faq awnsers
   *
   * @param message the question
   * @param role the role of the user
   * @return observable
   */
  getFaqAnswer(message: string, role: string): Observable<Faq> {
    let queryParams = new HttpParams();
    queryParams = queryParams.set('message', message).set('role', role);
    return this.http.get<Faq>(baseUri, {params: queryParams});
  }

}
