import { LOCALE_ID } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { describe } from 'vitest';
import { it } from 'vitest';
import { expect } from 'vitest';
import { IntegerFormatPipe } from './integer-format.pipe';

describe('integer format pipe', () => {
  it('transform', () => {
    TestBed.configureTestingModule({
      providers: [{ provide: LOCALE_ID, useValue: 'en' }],
    });

    TestBed.runInInjectionContext((): void => {
      expect(new IntegerFormatPipe().transform(1)).toEqual('1');
      expect(new IntegerFormatPipe().transform(12)).toEqual('12');
      expect(new IntegerFormatPipe().transform(123)).toEqual('123');
      expect(new IntegerFormatPipe().transform(1324)).toEqual('1.324');
      expect(new IntegerFormatPipe().transform(12345)).toEqual('12.345');
      expect(new IntegerFormatPipe().transform(123456)).toEqual('123.456');
      expect(new IntegerFormatPipe().transform(1234567)).toEqual('1.234.567');
      expect(new IntegerFormatPipe().transform(12345678)).toEqual('12.345.678');
      expect(new IntegerFormatPipe().transform(123456789)).toEqual('123.456.789');
      expect(new IntegerFormatPipe().transform(1234567890)).toEqual('1.234.567.890');
    });
  });

  it('transform fr', () => {
    TestBed.configureTestingModule({
      providers: [{ provide: LOCALE_ID, useValue: 'fr' }],
    });

    TestBed.runInInjectionContext((): void => {
      expect(new IntegerFormatPipe().transform(1)).toEqual('1');
      expect(new IntegerFormatPipe().transform(12)).toEqual('12');
      expect(new IntegerFormatPipe().transform(123)).toEqual('123');
      expect(new IntegerFormatPipe().transform(1324)).toEqual('1\u2009324');
      expect(new IntegerFormatPipe().transform(12345)).toEqual('12\u2009345');
      expect(new IntegerFormatPipe().transform(123456)).toEqual('123\u2009456');
      expect(new IntegerFormatPipe().transform(1234567)).toEqual('1\u2009234\u2009567');
      expect(new IntegerFormatPipe().transform(12345678)).toEqual('12\u2009345\u2009678');
      expect(new IntegerFormatPipe().transform(123456789)).toEqual('123\u2009456\u2009789');
      expect(new IntegerFormatPipe().transform(1234567890)).toEqual('1\u2009234\u2009567\u2009890');
    });
  });
});
