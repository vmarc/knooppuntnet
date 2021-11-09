import { LOCALE_ID } from '@angular/core';
import { Inject } from '@angular/core';
import { Pipe, PipeTransform } from '@angular/core';
import { Timestamp } from '@api/custom/timestamp';
import { TimestampUtil } from '../timestamp-util';

@Pipe({
  name: 'yyyymmdd',
})
export class TimestampDayPipe implements PipeTransform {
  constructor(@Inject(LOCALE_ID) public locale: string) {}

  transform(timestamp: Timestamp): string {
    const year = TimestampUtil.year(timestamp);
    const month = TimestampUtil.month(timestamp);
    const day = TimestampUtil.dayPart(timestamp);

    if (this.locale === 'nl') {
      return `${day}-${month}-${year}`;
    }
    if (this.locale === 'de') {
      return `${day}.${month}.${year}`;
    }
    if (this.locale === 'fr') {
      return `${day}/${month}/${year}`;
    }

    return `${year}-${month}-${day}`;
  }
}
