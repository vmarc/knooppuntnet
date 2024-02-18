import { LOCALE_ID } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { Timestamp } from '@api/custom';
import { TimestampPipe } from './timestamp-pipe';

describe('timstamp pipe', () => {
  it('transform', () => {
    const timestamp: Timestamp = '2021-08-11T12:34:56Z';
    const buildPipe = (language) => {
      return TestBed.configureTestingModule({
        providers: [{ provide: LOCALE_ID, useValue: language }],
      }).inject(TimestampPipe);
    };
    expect(buildPipe('en').transform(timestamp)).toEqual('2021-08-11 12:34');
    expect(buildPipe('nl').transform(timestamp)).toEqual('11-08-2021 12:34');
    expect(buildPipe('de').transform(timestamp)).toEqual('11.08.2021 12:34');
    expect(buildPipe('fr').transform(timestamp)).toEqual('11/08/2021 12:34');
  });
});
