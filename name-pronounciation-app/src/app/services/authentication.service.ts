import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Employee } from '@app/models';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private currentEmployeeSubject: BehaviorSubject<Employee>;
  public currentEmployee: Observable<Employee>;
  defaultURL: string = 'http://localhost:4200/employees/authenticate';

  constructor(private http: HttpClient) {
    this.currentEmployeeSubject = new BehaviorSubject<Employee>(JSON.parse(localStorage.getItem('currentEmployee')!));
    this.currentEmployee = this.currentEmployeeSubject.asObservable();
  }

  public get currentEmployeeValue(): Employee {
    return this.currentEmployeeSubject.value;
  }

  login(username: string, password: string) {
    return this.http.post<any>(this.defaultURL, { username, password })
      .pipe(map(employee => {
        // login successful if there's a jwt token in the response
        if (employee && employee.token) {
          // store emp details and jwt token in local storage to keep emp logged in between page refreshes
          localStorage.setItem('currentEmployee', JSON.stringify(employee));
          this.currentEmployeeSubject.next(employee);
        }
        return employee;
      }))
  }

  logout() {
    // remove emp from local storage to log out
    localStorage.removeItem('currentEmployee');
    this.currentEmployeeSubject.next(null!);
  }
}
