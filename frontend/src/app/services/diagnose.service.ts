import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {Diagnose, Examination, Patient} from '../dtos/patient';

const baseUri = environment.backendUrl + '/api/v1/patients/';

@Injectable({
  providedIn: 'root'
})
export class DiagnoseService {
  constructor(
    private http: HttpClient,
  ) { }

  /**
   * Add a new diagnosis.
   *
   * @param diagnosis the diagnosis to create
   * @return observable diagnosis
   */
  addNewDiagnosis(diagnosis: Diagnose): Observable<Diagnose> {
    return this.http.post<Diagnose>(baseUri + diagnosis.patientId + '/diagnose', diagnosis);
  }

  /**
   * Add a new diagnosis.
   *
   * @param diagnosis the diagnosis to create
   * @return observable diagnosis
   */
  updateDiagnosis(diagnosis: Diagnose): Observable<Diagnose> {
    return this.http.put<Diagnose>(baseUri + diagnosis.patientId + '/diagnose/' + diagnosis.id, diagnosis);
  }

  /**
   * Delete an diagnosis from the system.
   *
   * @param id the id of the diagnosis that should be deleted
   * @param patientId the id of the patient that the diagnosis belongs to
   * @return an Observable for the deleted diagnosis
   */
  delete(id: number, patientId: number): Observable<Diagnose> {
    return this.http.delete<Diagnose>(
      baseUri + patientId + '/diagnose/' + id
    );
  }

  /**
   * Load an examination from the system.
   *
   * @param id the id of the examination that should be loaded
   * @param patientId the id of the patient that the examination belongs to
   * @return an Observable for the loaded examination
   */
  load(id: number, patientId: number): Observable<Diagnose> {
    return this.http.get<Diagnose>(
      baseUri + patientId + '/diagnose/' + id
    );
  }
}
