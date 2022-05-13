import { InjectionToken } from "@angular/core";

export const NavigatorToken = new InjectionToken("Navigator");
export function navigatorProvider() { return navigator; }