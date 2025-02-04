import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Ref } from '@api/common/common';
import { RouteType } from '@api/common';
import { IconRouteComponent } from '@app/components/shared/icon';
import { LinkRouteComponent } from '@app/components/shared/link';
import { ActionButtonRouteComponent } from '../../../../components/action/action-button-route.component';

@Component({
  selector: 'kpn-network-fact-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (route of routes(); track route.id) {
      <div class="kpn-align-center">
        <kpn-icon-route />
        <kpn-action-button-route [routeType]="routeType()" [relationId]="route.id" />
        <kpn-link-route [routeId]="route.id" [routeName]="route.name" [routeType]="routeType()" />
      </div>
    }
  `,
  imports: [LinkRouteComponent, ActionButtonRouteComponent, IconRouteComponent],
})
export class NetworkFactRoutesComponent {
  routeType = input.required<RouteType>();
  routes = input.required<Ref[]>();
}
