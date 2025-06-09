import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Timestamp } from '@api/custom/timestamp';
import { TimestampDayPipe } from '../format/timestamp-day.pipe';

@Component({
  selector: 'ui-day',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` {{ timestamp() | yyyymmdd }} `,
  imports: [TimestampDayPipe],
})
export class DayComponent {
  readonly timestamp = input.required<Timestamp>();
}
