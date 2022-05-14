import { Role } from "./role";

export interface Employee {
    employeeId: string;
    username: string;
    password: string;
    role: Role;
    token?: string;
    legalFirstName: string;
    //middleName: string;
    legalLastName?: string;
    preferredName?: string;
}