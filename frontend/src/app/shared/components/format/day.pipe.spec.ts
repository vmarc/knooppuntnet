import { LOCALE_ID } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { Day } from '@api/custom/day';
import { DayPipe } from './day.pipe';

describe('DayPipe', () => {
  it('transform', () => {
    const day1: Day = '2020-08';
    const day2: Day = '2020-08-11';

    TestBed.configureTestingModule({
      providers: [{ provide: LOCALE_ID, useValue: 'en' }],
    });
    TestBed.runInInjectionContext((): void => {
      const pipe = new DayPipe();
      expect(pipe).toBeTruthy();
      expect(pipe.transform(null)).toEqual('-');

      expect(pipe.transform(day1)).toEqual('2020-08');
      expect(pipe.transform(day2)).toEqual('2020-08-11');
    });
  });
});
