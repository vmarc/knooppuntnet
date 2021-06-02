import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Timestamp } from '@api/custom/timestamp';
import { TimestampUtil } from '../timestamp-util';

@Component({
  selector: 'kpn-day',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` {{ formattedTimestamp() }} `,
})
export class DayComponent {
  @Input() timestamp: Timestamp;

  formattedTimestamp() {
    if (!this.timestamp) {
      return '';
    }
    return TimestampUtil.day(this.timestamp);
  }
}
