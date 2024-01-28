import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Ref } from '@api/common/common';
import { NetworkType } from '@api/custom';
import { LinkRouteComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-network-fact-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,

  template: `
    @if (routes().length === 1) {
      <span i18n="@@network-facts.route" class="kpn-label">Route</span>
    } @else if (routes().length > 1) {
      <span i18n="@@network-facts.routes" class="kpn-label">Routes</span>
    }
    <div class="kpn-comma-list">
      @for (route of routes(); track route.id) {
        <kpn-link-route
          [routeId]="route.id"
          [routeName]="route.name"
          [networkType]="networkType()"
        />
      }
    </div>
  `,
  standalone: true,
  imports: [LinkRouteComponent],
})
export class NetworkFactRoutesComponent {
  networkType = input<NetworkType | undefined>();
  routes = input<Ref[] | undefined>();
}
