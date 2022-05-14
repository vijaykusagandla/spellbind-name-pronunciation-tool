import { TestBed } from '@angular/core/testing';

import { DefaultRolesInterceptor } from './default-roles.interceptor';

describe('DefaultRolesInterceptor', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      DefaultRolesInterceptor
      ]
  }));

  it('should be created', () => {
    const interceptor: DefaultRolesInterceptor = TestBed.inject(DefaultRolesInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
