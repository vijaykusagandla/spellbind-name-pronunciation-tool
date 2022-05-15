import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthenticationService } from '@app/services';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {

    constructor(
        private router: Router,
        private authenticationService: AuthenticationService
    ) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const currentEmployee = this.authenticationService.currentEmployeeValue;
        if (currentEmployee) {
            // check if route is restricted by role
            if (route.data.role && route.data.role.indexOf(currentEmployee.role) === -1) {
                // role not authorized so redirect to home page
                this.router.navigate(['/']);
                return false;
            }
            // authorized 
            return true;
        }
        // not logged in, redirect to login page 
        this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
        return false;
    }
}
