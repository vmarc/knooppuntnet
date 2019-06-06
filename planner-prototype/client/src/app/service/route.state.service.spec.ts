import { TestBed, inject } from '@angular/core/testing';

import { Route.StateService } from './route.state.service';

describe('Route.StateService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [Route.StateService]
    });
  });

  it('should be created', inject([Route.StateService], (service: Route.StateService) => {
    expect(service).toBeTruthy();
  }));
});
