import { Pipe, PipeTransform } from '@angular/core';
import { Timestamp } from '@api/custom/timestamp';
import { TimestampUtil } from '../timestamp-util';

@Pipe({
  name: 'yyyymmddhhmm',
})
export class TimestampPipe implements PipeTransform {
  transform(timestamp: Timestamp): string {
    return TimestampUtil.formatted(timestamp);
  }
}
