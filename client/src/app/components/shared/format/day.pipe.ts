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
      return DayUtil.toString(this.locale, day);
    }
    return '-';
  }
}
