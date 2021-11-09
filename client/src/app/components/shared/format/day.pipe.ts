import { LOCALE_ID } from '@angular/core';
import { Inject } from '@angular/core';
import { Pipe, PipeTransform } from '@angular/core';
import { Day } from '@api/custom/day';
import { DayUtil } from '../day-util';

@Pipe({
  name: 'day',
})
export class DayPipe implements PipeTransform {
  constructor(@Inject(LOCALE_ID) public locale: string) {}

  transform(day: Day): string {
    if (day) {
      const year = DayUtil.year(day);
      const month = DayUtil.month(day);
      if (day.length > '2021-08'.length) {
        const dayPart = DayUtil.day(day);
        if (this.locale === 'nl') {
          return `${dayPart}-${month}-${year}`;
        }
        if (this.locale === 'de') {
          return `${dayPart}.${month}.${year}`;
        }
        if (this.locale === 'fr') {
          return `${dayPart}/${month}/${year}`;
        }
        return `${year}-${month}-${dayPart}`;
      }
      if (this.locale === 'nl') {
        return `${month}-${year}`;
      }
      if (this.locale === 'de') {
        return `${month}.${year}`;
      }
      if (this.locale === 'fr') {
        return `${month}/${year}`;
      }
      return `${year}-${month}`;
    }
    return '-';
  }
}
