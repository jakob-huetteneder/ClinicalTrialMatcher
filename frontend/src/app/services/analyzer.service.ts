import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {PythonResponse} from '../dtos/patient';

const baseUri = environment.analyzerUrl + '/extract_entities';

@Injectable({
  providedIn: 'root'
})
export class AnalyzerService {
  constructor(
    private http: HttpClient,
  ) { }

  /**
   * Analyze and admission note.
   *
   * @param note to analyze
   * @return observable list of diseases
   */
  analyzeNote(note: string): Observable<string[]> {
    return this.http.post<string[]>(baseUri, note);
  }

  /**
   * Analyze and admission note.
   *
   * @param note to analyze
   * @return observable list of diseases and negatives
   */
  analyzeNoteNegatives(note: string): Observable<PythonResponse> {
    return this.http.put<PythonResponse>(baseUri, note);
  }

}
