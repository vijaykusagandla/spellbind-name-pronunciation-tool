import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Employee } from '@app/models';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  apiURL: string = 'http://localhost:8080/spellbind/listEmployees';

  constructor(private http: HttpClient) { }

  getAllEmployees() {
    return this.http.get<Employee[]>(this.apiURL);
  }
}
