import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {Disease} from '../dtos/patient';

const baseUri = environment.backendUrl + '/api/v1/diseases';

@Injectable({
  providedIn: 'root'
})
export class DiseaseService {
  constructor(
    private http: HttpClient,
  ) { }

  public searchByName(name: string, limitTo: number): Observable<Disease[]> {
    const params = new HttpParams()
      .set('name', name)
      .set('limit', limitTo);
    return this.http.get<Disease[]>(baseUri, { params });
  }
}
