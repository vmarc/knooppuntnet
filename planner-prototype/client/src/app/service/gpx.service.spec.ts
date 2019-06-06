import { TestBed, inject } from '@angular/core/testing';

import { GpxService } from './gpx.service';

describe('GpxService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GpxService]
    });
  });

  it('should be created', inject([GpxService], (service: GpxService) => {
    expect(service).toBeTruthy();
  }));
});
