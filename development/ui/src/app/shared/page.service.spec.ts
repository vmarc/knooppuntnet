import { TestBed } from '@angular/core/testing';

import { PageService } from './page.service';

describe('PageService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PageService = TestBed.get(PageService);
    expect(service).toBeTruthy();
  });
});
