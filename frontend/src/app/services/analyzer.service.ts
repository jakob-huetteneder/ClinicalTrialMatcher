import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {PythonResponse} from '../dtos/patient';

const baseUri = environment.analyzerUrl;

@Injectable({
  providedIn: 'root'
})
export class AnalyzerService {
  constructor(
    private http: HttpClient,
  ) { }

  /**
   * Analyze an admission note.
   *
   * @param note to analyze
   * @return observable list of diseases
   */
  analyzeNote(note: string): Observable<string[]> {
    return this.http.post<string[]>(baseUri + '/extract_entities', note);
  }

  /**
   * Analyze and admission note.
   *
   * @param note to analyze
   * @return observable list of diseases and negatives
   */
  analyzeNoteNegatives(note: string): Observable<PythonResponse> {
    return this.http.put<PythonResponse>(baseUri + '/extract_entities', note);
  }

  /**
   * Analyze for gender.
   *
   * @param note to analyze
   * @returns: observable gender
   */
  analyzeGender(note: string): Observable<string> {
    return this.http.post<string>(baseUri + '/extract_gender', note);
  }
}
