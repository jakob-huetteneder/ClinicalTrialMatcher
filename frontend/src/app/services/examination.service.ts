import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {Examination, Patient} from '../dtos/patient';

const baseUri = environment.backendUrl + '/api/v1/patients/';

@Injectable({
  providedIn: 'root'
})
export class ExaminationService {
  constructor(
    private http: HttpClient,
  ) { }

  /**
   * Add a new examination.
   *
   * @param examination the examination to create
   * @return observable examination
   */
  addNewExamination(examination: Examination): Observable<Examination> {
    return this.http.post<Examination>(baseUri + examination.patientId + '/examination', examination);
  }

  /**
   * Add a new examination.
   *
   * @param examination the examination to create
   * @return observable examination
   */
  updateExamination(examination: Examination): Observable<Examination> {
    return this.http.put<Examination>(baseUri + examination.patientId + '/examination/' + examination.id, examination);
  }

  /**
   * Delete an examination from the system.
   *
   * @param id the id of the examination that should be deleted
   * @param patientId the id of the patient that the examination belongs to
   * @return an Observable for the deleted examination
   */
  delete(id: number, patientId: number): Observable<Examination> {
    return this.http.delete<Examination>(
      baseUri + patientId + '/examination/' + id
    );
  }

  /**
   * Load an examination from the system.
   *
   * @param id the id of the examination that should be loaded
   * @param patientId the id of the patient that the examination belongs to
   * @return an Observable for the loaded examination
   */
  load(id: number, patientId: number): Observable<Examination> {
    return this.http.get<Examination>(
      baseUri + patientId + '/examination/' + id
    );
  }
}
