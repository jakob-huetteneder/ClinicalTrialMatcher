import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../dtos/user';
import {environment} from '../../environments/environment';

const baseUri = environment.backendUrl + '/api/v1/users';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(
    private http: HttpClient,
  ) { }

  /**
   * Get all users
   *
   * @return observable list of all users.
   */
  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(baseUri);
  }

  /**
   * Update a user
   *
   * @param user the user to update
   * @return observable user
   */
  updateUser(user: User): Observable<User> {
    return this.http.put<User>(baseUri + '/' + user.id, user);
  }

  /**
   * Delete a user
   *
   * @param userId the id of the user to delete
   * @return observable user
   */
  deleteUser(userId: number): Observable<void> {
    return this.http.delete<void>(baseUri + '/' + userId);
  }

  /**
   * Register a new user.
   *
   * @param user the user to create
   * @return observable user
   */
  createUser(user: User): Observable<User> {
    return this.http.post<User>(baseUri, user);
  }
}
