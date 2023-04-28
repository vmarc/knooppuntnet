import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Timestamp } from '@api/custom';

@Component({
  selector: 'kpn-day',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` {{ timestamp | yyyymmdd }} `,
})
export class DayComponent {
  @Input() timestamp: Timestamp;
}
