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

    // Do not intercept authentication requests
    if (req.url === authUri || (req.url === usersUri && req.method === 'POST')) {
      return next.handle(req);
    }
    const url = window.location.href;
    if (!url.includes('password/password')) {
      const authReq = req.clone({
        headers: req.headers.set('Authorization', 'Bearer ' + this.authService.getToken())
      });
      return next.handle(authReq);
    } else {
      const authReq = req.clone();
      return next.handle(authReq);
    }
  }
}
