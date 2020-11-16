import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {RouteDiffsData} from './route-diffs-data';

@Component({
  selector: 'kpn-route-diffs',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-route-diffs-removed [data]="data"></kpn-route-diffs-removed>
    <kpn-route-diffs-added [data]="data"></kpn-route-diffs-added>
    <kpn-route-diffs-updated [data]="data"></kpn-route-diffs-updated>
  `
})
export class RouteDiffsComponent {
  @Input() data: RouteDiffsData;
}
