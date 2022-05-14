import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from './services';
import { Employee, Role } from './models';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'name-pronounciation-app';
  currentEmployee: Employee;

  constructor(private router: Router, private authenticationService: AuthenticationService) {
    this.authenticationService.currentEmployee.subscribe(x => this.currentEmployee = x);
  }

  get isAdmin() {
    return this.currentEmployee && this.currentEmployee.role === Role.Admin;
  }

  logOut() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }
}