import { LOCALE_ID } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { TimestampPipe } from './timestamp-pipe';

describe('timstamp pipe', () => {
  const testPipe = (language, expected) => {
    TestBed.configureTestingModule({
      providers: [{ provide: LOCALE_ID, useValue: language }],
    });
    TestBed.runInInjectionContext((): void => {
      expect(new TimestampPipe().transform('2021-08-11T12:34:56Z')).toEqual(expected);
    });
  };

  it('en', () => {
    testPipe('en', '2021-08-11 12:34');
  });

  it('nl', () => {
    testPipe('nl', '11-08-2021 12:34');
  });

  it('fr', () => {
    testPipe('fr', '11/08/2021 12:34');
  });

  it('de', () => {
    testPipe('de', '11.08.2021 12:34');
  });
});
