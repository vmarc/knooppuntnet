import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { Timestamp } from '@api/custom';
import { TimestampDayPipe } from '../format/timestamp-day.pipe';

@Component({
  selector: 'kpn-day',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` {{ timestamp | yyyymmdd }} `,
  standalone: true,
  imports: [TimestampDayPipe],
})
export class DayComponent {
  @Input() timestamp: Timestamp;
}
