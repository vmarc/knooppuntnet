import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteType } from '@api/common';
import { RouteTypeIconComponent } from './route-type-icon.component';
import { RouteTypeNameComponent } from './route-type-name.component';

@Component({
  selector: 'kpn-route-type',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="route-type">
      <kpn-route-type-icon [routeType]="routeType()" />
      <kpn-route-type-name [routeType]="routeType()" />
      <ng-content></ng-content>
    </div>
  `,
  styles: `
    .route-type {
      display: inline-flex;
      flex-direction: row;
      align-items: center;
    }

    kpn-route-type-icon {
      height: 24px;
      margin-right: 10px;
    }
  `,
  imports: [RouteTypeIconComponent, RouteTypeNameComponent],
})
export class RouteTypeComponent {
  routeType = input.required<RouteType>();
}
