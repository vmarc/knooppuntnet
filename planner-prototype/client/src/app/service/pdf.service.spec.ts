import { TestBed, inject } from '@angular/core/testing';

import { PDFService } from './pdf.service';

describe('PDFService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PDFService]
    });
  });

  it('should be created', inject([PDFService], (service: PDFService) => {
    expect(service).toBeTruthy();
  }));
});
