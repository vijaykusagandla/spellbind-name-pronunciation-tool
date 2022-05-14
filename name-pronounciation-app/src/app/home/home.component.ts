import { Component, OnInit } from '@angular/core';
import { Employee } from '@app/models';
import { EmployeeService, AuthenticationService } from '@app/services';
import { ColDef, FirstDataRenderedEvent } from 'ag-grid-community';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  loading = false;
  currentEmployee: Employee;

  columnDefs: ColDef[] = [
    { field: "employeeId", sortable: true },
    { field: "legalFirstName", sortable: true },
    { field: "legalLastName", sortable: true },
    { field: "preferredName", sortable: true }
  ];

  public defaultColDef: ColDef = {
    resizable: true,
  };

  rowData = [
    { employeeId: '10001', legalFirstName: 'MIA', legalLastName: 'YANG', preferredName: 'DIVI' },
    { employeeId: '10002', legalFirstName: 'JOANNA', legalLastName: 'SMITH', preferredName: 'JOAN' }
  ];
  //rowData: Observable<Employee[]>


  constructor(private empService: EmployeeService,
    private authService: AuthenticationService) {
    this.currentEmployee = this.authService.currentEmployeeValue;
    //this.rowData = this.empService.getAllEmployees();
  }

  ngOnInit(): void {
  }

  onFirstDataRendered(params: FirstDataRenderedEvent) {
    params.api.sizeColumnsToFit();
  }

}
