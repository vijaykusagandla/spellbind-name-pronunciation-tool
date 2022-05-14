import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationService } from '@app/services';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private authenticationService: AuthenticationService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // add auth header with jwt if user is logged in and request is to api url
    const currentEmployee = this.authenticationService.currentEmployeeValue;
    const isLoggedIn = currentEmployee && currentEmployee.token;
    const isApiUrl = request.url.startsWith('http://localhost:4200');
    if (isLoggedIn && isApiUrl) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${currentEmployee.token}`
        }
      })
    }
    return next.handle(request);
  }
}
