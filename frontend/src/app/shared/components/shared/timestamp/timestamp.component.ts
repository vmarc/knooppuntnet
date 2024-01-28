import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Timestamp } from '@api/custom';
import { TimestampPipe } from '../format/timestamp-pipe';

@Component({
  selector: 'kpn-timestamp',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` {{ timestamp() | yyyymmddhhmm }} `,
  standalone: true,
  imports: [TimestampPipe],
})
export class TimestampComponent {
  timestamp = input.required<Timestamp>();
}
