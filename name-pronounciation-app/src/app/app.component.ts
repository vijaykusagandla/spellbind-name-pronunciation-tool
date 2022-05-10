import { Component } from '@angular/core';
import { Employee } from './data/model/employee';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ColDef, FirstDataRenderedEvent, GridReadyEvent } from 'ag-grid-community';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'name-pronounciation-app';
  apiURL: string = 'http://localhost:4200/spellbind';
  
   httpOptions = {
    headers: new HttpHeaders({
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    })
};

  columnDefs : ColDef[] = [
    { field: "employeeId", sortable: true, filter: true, minWidth: 50 },
    { field: "firstName", sortable: true, filter: true, minWidth: 150 },
    { field: "middleName" , sortable: true, filter: true, minWidth: 150},
    { field: "lastName" , sortable: true, filter: true, minWidth: 70},
    { field: "preferredName" , sortable: true, filter: true, minWidth: 80},
    { field: "dateOfBirth" , sortable: true, filter: true, minWidth: 100},
    { field: "emailAddress" , sortable: true, filter: true, minWidth: 100},
    { field: "addressLine1" , sortable: true, filter: true, minWidth: 100},
    { field: "addressLine2" , sortable: true, filter: true, minWidth: 100},
    { field: "zipCode" , sortable: true, filter: true, minWidth: 100},
    { field: "phoneNumber" , sortable: true, filter: true, minWidth: 100}
  ];

  public defaultColDef: ColDef = {
    resizable: true,
  };

  rowData: Observable<Employee[]>

	constructor(private http: HttpClient) {
		this.rowData = this.http.get<Employee[]>('http://localhost:8080/spellbind/listEmployees');
	}
	
  onFirstDataRendered(params: FirstDataRenderedEvent) {
    params.api.sizeColumnsToFit();
  }

}
