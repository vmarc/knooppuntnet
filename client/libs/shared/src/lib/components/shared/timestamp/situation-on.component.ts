import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { Timestamp } from '@api/custom';
import { TimestampComponent } from './timestamp.component';

@Component({
  selector: 'kpn-situation-on',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ng-container class="kpn-label" i18n="@@situation-on">
      Situation on
    </ng-container>
    <kpn-timestamp [timestamp]="timestamp" />
  `,
  standalone: true,
  imports: [TimestampComponent],
})
export class SituationOnComponent {
  @Input() timestamp: Timestamp;
}
