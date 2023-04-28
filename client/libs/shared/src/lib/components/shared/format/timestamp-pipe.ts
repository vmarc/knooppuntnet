import { LOCALE_ID } from '@angular/core';
import { Inject } from '@angular/core';
import { Pipe, PipeTransform } from '@angular/core';
import { Timestamp } from '@api/custom';
import { TimestampUtil } from '..';

@Pipe({
  name: 'yyyymmddhhmm',
  standalone: true,
})
export class TimestampPipe implements PipeTransform {
  constructor(@Inject(LOCALE_ID) public locale: string) {}

  transform(timestamp: Timestamp): string {
    const year = TimestampUtil.year(timestamp);
    const month = TimestampUtil.month(timestamp);
    const day = TimestampUtil.dayPart(timestamp);
    const hour = TimestampUtil.hour(timestamp);
    const minute = TimestampUtil.minute(timestamp);

    if (this.locale === 'nl') {
      return `${day}-${month}-${year} ${hour}:${minute}`;
    }
    if (this.locale === 'de') {
      return `${day}.${month}.${year} ${hour}:${minute}`;
    }
    if (this.locale === 'fr') {
      return `${day}/${month}/${year} ${hour}:${minute}`;
    }

    return `${year}-${month}-${day} ${hour}:${minute}`;
  }
}
