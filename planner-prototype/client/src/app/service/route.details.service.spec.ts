import { TestBed, inject } from '@angular/core/testing';

import { Route.DetailsService } from './route.details.service';

describe('Route.DetailsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [Route.DetailsService]
    });
  });

  it('should be created', inject([Route.DetailsService], (service: Route.DetailsService) => {
    expect(service).toBeTruthy();
  }));
});
