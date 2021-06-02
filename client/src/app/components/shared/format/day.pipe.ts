import { Pipe, PipeTransform } from '@angular/core';
import { Day } from '@api/custom/day';

@Pipe({
  name: 'day',
})
export class DayPipe implements PipeTransform {
  transform(day: Day): string {
    if (day) {
      return day;
    }
    return '-';
  }
}
