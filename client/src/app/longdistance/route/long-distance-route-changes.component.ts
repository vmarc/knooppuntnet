import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'kpn-long-distance-route-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-long-distance-route-page-header pageName="changes"></kpn-long-distance-route-page-header>
  `,
  styles: [`
  `]
})
export class LongDistanceRouteChangesComponent {
}
