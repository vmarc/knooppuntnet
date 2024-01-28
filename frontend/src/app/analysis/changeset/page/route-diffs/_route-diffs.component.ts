import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteDiffsAddedComponent } from './route-diffs-added.component';
import { RouteDiffsData } from './route-diffs-data';
import { RouteDiffsRemovedComponent } from './route-diffs-removed.component';
import { RouteDiffsUpdatedComponent } from './route-diffs-updated.component';

@Component({
  selector: 'kpn-route-diffs',
  changeDetection: ChangeDetectionStrategy.OnPush,

  template: `
    <kpn-route-diffs-removed [data]="data()" />
    <kpn-route-diffs-added [data]="data()" />
    <kpn-route-diffs-updated [data]="data()" />
  `,
  standalone: true,
  imports: [RouteDiffsAddedComponent, RouteDiffsRemovedComponent, RouteDiffsUpdatedComponent],
})
export class RouteDiffsComponent {
  data = input<RouteDiffsData | undefined>();
}
