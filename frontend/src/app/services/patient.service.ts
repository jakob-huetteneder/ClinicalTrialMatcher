import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {Patient, PatientRequest} from '../dtos/patient';

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
   * Get all patients.
   *
   * @param search a string that has to be included in the patient representation
   *
   * @return observable list of patients
   */
  getAllPatientsToRequest(search: string): Observable<PatientRequest[]> {
    const params = {params: new HttpParams().set('search', search)};
    return this.http.get<PatientRequest[]>(baseUri, params);
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
