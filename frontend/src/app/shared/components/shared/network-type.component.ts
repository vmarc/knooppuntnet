import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteType } from '@api/common';
import { RouteTypeIconComponent } from './network-type-icon.component';
import { RouteTypeNameComponent } from './route-type-name.component';

@Component({
  selector: 'kpn-network-type',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="network-type">
      <kpn-network-type-icon [routeType]="routeType()" />
      <kpn-network-type-name [routeType]="routeType()" />
      <ng-content></ng-content>
    </div>
  `,
  styles: `
    .network-type {
      display: inline-flex;
      flex-direction: row;
      align-items: center;
    }

    kpn-network-type-icon {
      height: 24px;
      margin-right: 10px;
    }
  `,
  imports: [RouteTypeIconComponent, RouteTypeNameComponent],
})
export class RouteTypeComponent {
  routeType = input.required<RouteType>();
}
