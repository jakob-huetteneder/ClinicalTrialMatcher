import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {AuthService} from '../services/auth.service';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService, private globals: Globals) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const authUri = this.globals.backendUri + '/authentication';
    const usersUri = this.globals.backendUri + '/users';
    const trialsUri = this.globals.backendUri + '/trials';
    const searchUri = this.globals.backendUri + '/trials/search';
    const passwordUri = this.globals.backendUri + '/users/password';

    // Do not intercept authentication requests
    if (req.url === authUri // all auth requests
      || (req.url === usersUri && req.method === 'POST') // registration of new users
      || (req.url === passwordUri) // password reset
      || (req.url === trialsUri && req.method === 'GET') // get all trials
      || (req.url.startsWith(searchUri)) // search for trials
      || (req.url.match(/\/trials\/[0-9]+/) && req.method === 'GET') // get trial by id
    ) {
      return next.handle(req);
    }

    const authReq = req.clone({
      headers: req.headers.set('Authorization', 'Bearer ' + this.authService.getToken())
    });

    return next.handle(authReq);
  }
}
