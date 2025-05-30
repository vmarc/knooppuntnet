import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Ref } from '@api/common/common/ref';
import { RouteType } from '@api/common/route-type';
import { IconRouteComponent } from '@app/shared/components/icon/icon-route.component';
import { LinkRouteComponent } from '@app/shared/components/link/link-route.component';
import { ActionButtonRouteComponent } from '../../../../components/action/action-button-route.component';

@Component({
  selector: 'ui-network-fact-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (route of routes(); track route.id) {
      <div class="kpn-align-center">
        <ui-icon-route />
        <ui-action-button-route [routeType]="routeType()" [relationId]="route.id" />
        <ui-link-route [routeId]="route.id" [routeName]="route.name" [routeType]="routeType()" />
      </div>
    }
  `,
  imports: [LinkRouteComponent, ActionButtonRouteComponent, IconRouteComponent],
})
export class NetworkFactRoutesComponent {
  routeType = input.required<RouteType>();
  routes = input.required<Ref[]>();
}
