import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkNodeRow } from '@api/common/network';
import { LinkRouteComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-network-node-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,

  template: `
    @if (node().routeReferences.length === 0) {
      <span class="warning" i18n="@@network-nodes.no-routes">no routes</span>
    } @else {
      <span class="kpn-comma-list">
        @for (ref of node().routeReferences; track $index) {
          <kpn-link-route
            [routeId]="ref.id"
            [routeName]="ref.name"
            [networkType]="ref.networkType"
          />
        }
      </span>
    }
  `,
  standalone: true,
  imports: [LinkRouteComponent],
})
export class NetworkNodeRoutesComponent {
  node = input<NetworkNodeRow | undefined>();
}
