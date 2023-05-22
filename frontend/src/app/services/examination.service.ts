import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
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
   * Delete an examination by id.
   *
   * @param examination the examination to delete
   * @return observable examination
   *//*
  deleteExamination(examination: Examination): Observable<Examination> {
    return this.http.delete<Examination>(baseUri + examination.patientId + '/examination/' + examination.id, examination);
  }*/

  /**
   * View an examination by id.
   *
   * @param examination the examination to view
   * @return observable examination
   *//*
  viewExamination(examination: Examination): Observable<Examination> {
    return this.http.get<Examination>(baseUri + examination.patientId + '/examination/' + examination.id, examination);
  }*/
}
