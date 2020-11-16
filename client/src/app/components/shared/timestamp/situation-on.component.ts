import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {Timestamp} from '../../../kpn/api/custom/timestamp';

@Component({
  selector: 'kpn-situation-on',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ng-container class="kpn-label" i18n="@@situation-on">Situation on</ng-container>
    <kpn-timestamp [timestamp]="timestamp"></kpn-timestamp>
  `
})
export class SituationOnComponent {

  @Input() timestamp: Timestamp;

}
