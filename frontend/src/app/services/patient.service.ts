import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {Patient} from '../dtos/patient';

const baseUri = environment.backendUrl + '/api/v1/patients';

@Injectable({
  providedIn: 'root'
})
export class PatientService {
  constructor(
    private http: HttpClient,
  ) { }

  /**
   * Register a new patient.
   *
   * @param patient the patient to create
   * @return observable patient
   */
  createPatient(patient: Patient): Observable<Patient> {
    return this.http.post<Patient>(baseUri, patient);
  }

  /**
   * Get a patient by id.
   *
   * @param id of patient
   * @return observable patient
   */
  getById(id: number): Observable<Patient> {
    return this.http.get<Patient>(baseUri + '/' + id);
  }

  /**
   * Get a patient by id.
   *
   * @param id of patient
   * @return observable patient
   */
  deleteById(id: number): Observable<Patient> {
    return this.http.delete<Patient>(baseUri + '/' + id);
  }
}
