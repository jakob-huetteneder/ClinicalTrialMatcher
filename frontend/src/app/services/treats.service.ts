import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {PatientRequest, Treats} from '../dtos/patient';

const baseUri = environment.backendUrl + '/api/v1/treats';

@Injectable({
  providedIn: 'root'
})
export class TreatsService {
  constructor(
    private http: HttpClient,
  ) {
  }

  /**
   * Get all requests for the logged in.
   *
   * @Retruns a list of requests from doctors to treat the logged in patient
   */
  getAllRequests(search: string): Observable<Treats[]> {
    const params = {params: new HttpParams().set('search', search)};
    return this.http.get<Treats[]>(baseUri + '/requests', params);
  }

  /**
   * Request a treating relationship to a patient (as a doctor).
   *
   * @param patientId the patient to treat
   */
  requestTreats(patientId: number): Observable<PatientRequest> {
    return this.http.post<PatientRequest>(baseUri + '/' + patientId, null);
  }

  /**
   * Respond to a request posed by the doctor
   *
   * @param doctorId the id of the doctor who sent the request
   * @param accept true: accept request, false: decline request
   */
  respondToRequest(doctorId: number, accept: boolean): Observable<Treats> {
    return this.http.put<Treats>(baseUri + '/' + doctorId, accept);
  }

  /**
   * Delete a treating relationship
   *
   * @param id of the person to delete the treating relationship with
   */
  deleteTreats(id: number): Observable<void> {
    return this.http.delete<void>(baseUri + '/' + id);
  }
}
