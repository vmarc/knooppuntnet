import { LOCALE_ID } from '@angular/core';
import { Inject } from '@angular/core';
import { PipeTransform } from '@angular/core';
import { Pipe } from '@angular/core';

@Pipe({ name: 'statistic' })
export class StatisticPipe implements PipeTransform {
  constructor(@Inject(LOCALE_ID) public locale: string) {}

  transform(value: number): string {
    if (!!value) {
      let thousandsSeparator = '.';
      if (this.locale === 'fr') {
        thousandsSeparator = ' ';
      }
      return value
        .toString()
        .replace(/\B(?=(\d{3})+(?!\d))/g, thousandsSeparator);
    }
    return '-';
  }
}
