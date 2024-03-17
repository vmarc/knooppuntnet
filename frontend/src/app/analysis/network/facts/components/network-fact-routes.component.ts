import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Ref } from '@api/common/common';
import { NetworkType } from '@api/custom';
import { IconRouteComponent } from '@app/components/shared/icon';
import { LinkRouteComponent } from '@app/components/shared/link';
import { ActionButtonRouteComponent } from '../../../components/action/action-button-route.component';

@Component({
  selector: 'kpn-network-fact-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (route of routes(); track route.id) {
      <div class="kpn-align-center">
        <kpn-icon-route />
        <kpn-action-button-route [relationId]="route.id" />
        <kpn-link-route
          [routeId]="route.id"
          [routeName]="route.name"
          [networkType]="networkType()"
        />
      </div>
    }
  `,
  standalone: true,
  imports: [LinkRouteComponent, ActionButtonRouteComponent, IconRouteComponent],
})
export class NetworkFactRoutesComponent {
  networkType = input.required<NetworkType>();
  routes = input.required<Ref[]>();
}
