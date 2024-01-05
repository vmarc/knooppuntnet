import { inject } from '@angular/core';
import { LOCALE_ID } from '@angular/core';
import { Inject } from '@angular/core';
import { Pipe, PipeTransform } from '@angular/core';
import { Timestamp } from '@api/custom';
import { TimestampUtil } from '..';

@Pipe({
  name: 'yyyymmdd',
  standalone: true,
})
export class TimestampDayPipe implements PipeTransform {
  public locale: string = inject(LOCALE_ID);

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
