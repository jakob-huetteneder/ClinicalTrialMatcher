import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {SafeUrl} from '@angular/platform-browser';

const baseUri = environment.backendUrl + '/api/v1/files';

@Injectable({
  providedIn: 'root'
})
export class FilesService {
  constructor(
    private http: HttpClient,
  ) { }

  /**
   * Upload a new picture.
   *
   * @return observable file
   * @param file
   * @param id
   */
  createImage(file: File, id: number) {
    const formData = new FormData();
    formData.append('file', file, file.name);
    return this.http.post(baseUri + '/' + id, formData);
  }

  /**
   * Get a picture by examination id.
   *
   * @param id of examination.
   * @return observable file
   */
  getById(id: number): Observable<ArrayBuffer> {
    return this.http.get(baseUri + '/' + id, {responseType: 'arraybuffer'});
  }

  /**
   * Delete a picture by examination id.
   *
   * @param id of examination.
   * @return observable file
   */
  deleteById(id: number): Observable<any> {
    return this.http.delete(baseUri + '/' + id);
  }
}
