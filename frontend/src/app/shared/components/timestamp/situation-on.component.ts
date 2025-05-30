import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Timestamp } from '@api/custom/timestamp';
import { TimestampComponent } from './timestamp.component';

@Component({
  selector: 'ui-situation-on',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <span class="kpn-label" i18n="@@situation-on">Situation on</span>
    <ui-timestamp [timestamp]="timestamp()" />
  `,
  imports: [TimestampComponent],
})
export class SituationOnComponent {
  timestamp = input.required<Timestamp>();
}
