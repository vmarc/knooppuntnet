import { LOCALE_ID } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { describe } from 'vitest';
import { it } from 'vitest';
import { expect } from 'vitest';
import { DistancePipe } from './distance.pipe';

describe('DistancePipe', () => {
  it('transform', () => {
    TestBed.configureTestingModule({
      providers: [{ provide: LOCALE_ID, useValue: 'en' }],
    });
    TestBed.runInInjectionContext((): void => {
      const pipe = new DistancePipe();
      expect(pipe).toBeTruthy();
      expect(pipe.transform(null)).toEqual('-');
      expect(pipe.transform(100)).toEqual('100 m');
      expect(pipe.transform(999)).toEqual('999 m');
      expect(pipe.transform(1000)).toEqual('1 km');
      expect(pipe.transform(1049)).toEqual('1 km');
      expect(pipe.transform(1050)).toEqual('1,1 km');
      expect(pipe.transform(1100)).toEqual('1,1 km');
      expect(pipe.transform(9949)).toEqual('9,9 km');
      expect(pipe.transform(9950)).toEqual('10 km');
      expect(pipe.transform(10000)).toEqual('10 km');
      expect(pipe.transform(1000000)).toEqual('1.000 km');
    });
  });

  it('transform', () => {
    TestBed.configureTestingModule({
      providers: [{ provide: LOCALE_ID, useValue: 'fr' }],
    });
    TestBed.runInInjectionContext((): void => {
      const pipe = new DistancePipe();
      expect(pipe.transform(1050)).toEqual('1,1 km');
      expect(pipe.transform(1000000)).toEqual('1\u2009000 km');
    });
  });
});
