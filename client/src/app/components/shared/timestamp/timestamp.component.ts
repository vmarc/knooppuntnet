import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Timestamp } from '@api/custom';

@Component({
  selector: 'kpn-timestamp',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` {{ timestamp | yyyymmddhhmm }} `,
})
export class TimestampComponent {
  @Input() timestamp: Timestamp;
}
