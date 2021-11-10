import { LOCALE_ID } from '@angular/core';
import { Inject } from '@angular/core';
import { PipeTransform } from '@angular/core';
import { Pipe } from '@angular/core';

@Pipe({ name: 'statistic' })
export class StatisticPipe implements PipeTransform {
  constructor(@Inject(LOCALE_ID) public locale: string) {}

  transform(value: string): string {
    if (!value || value === '-') {
      return '-';
    }
    if (this.locale === 'fr') {
      return value.replaceAll('.', ' ');
    }
    return value;
  }
}
