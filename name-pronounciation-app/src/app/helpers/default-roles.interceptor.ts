import { Injectable } from '@angular/core';
import { HttpRequest, HttpResponse, HttpHandler, HttpEvent, HttpInterceptor, HTTP_INTERCEPTORS } from '@angular/common/http';
import { Employee, Role } from '@app/models';
import { Observable, of, throwError } from 'rxjs';
import { delay, mergeMap, materialize, dematerialize } from 'rxjs/operators';

const employees: Employee[] = [
  { employeeId: '1', username: 'admin', password: 'admin', legalFirstName: 'Admin', legalLastName: 'Employee', preferredName: 'Admin', role: Role.Admin },
  { employeeId: '2', username: 'emp1', password: 'emp1', legalFirstName: 'Emp', legalLastName: 'Test', preferredName: 'Emp', role: Role.Employee }
];

@Injectable()
export class DefaultRolesInterceptor implements HttpInterceptor {

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const { url, method, headers, body } = request;

    return of(null)
      .pipe(mergeMap(handleRoute))
      .pipe(materialize())
      .pipe(delay(500))
      .pipe(dematerialize());

    function handleRoute() {
      switch (true) {
        case url.endsWith('/employees/authenticate') && method === 'POST':
          return authenticate();
        case url.endsWith('/listEmployees') && method === 'GET':
          return getUsers();
        default:
          // pass through any requests not handled above
          return next.handle(request);
      }
    }
    //route functions 

    function authenticate() {
      const { username, password } = body;
      const emp = employees.find(x => x.username === username && x.password === password);
      if (!emp) return error('Username or password is incorrect');
      return ok({
        employeeId: emp.employeeId,
        username: emp.username,
        firstName: emp.legalFirstName,
        lastName: emp.legalLastName,
        role: emp.role,
        token: `fake-jwt-token.${emp.employeeId}`
      });
    }

    function getUsers() {
      if (!isAdmin()) return unauthorized();
      return ok(employees);
    }

    //helper functions 

    function ok(body: any) {
      return of(new HttpResponse({ status: 200, body }));
    }

    function unauthorized() {
      return throwError({ status: 401, error: { message: 'unauthorized' } });
    }

    function error(message: any) {
      return throwError({ status: 400, error: { message } });
    }

    function isLoggedIn() {
      return headers.get('Authorization') === 'Bearer fake-jwt-token';
    }

    function isAdmin() {
      return isLoggedIn() && currentEmployee()?.role === Role.Admin;
    }

    function currentEmployee() {
      if (!isLoggedIn()) return;
      const id = parseInt(headers.get('Authorization') || '');
      return employees.find(x => x.employeeId);
    }

  }
}

export const defaultRolesProvider = {
  // use fake backend in place of Http service for backend-less development
  provide: HTTP_INTERCEPTORS,
  useClass: DefaultRolesInterceptor,
  multi: true
};
