import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteType } from '@api/common/route-type';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { RouteTypeNameComponent } from './route-type-name.component';

@Component({
  selector: 'kpn-route-type',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="route-type">
      <nz-icon [nzType]="routeType()" />
      <kpn-route-type-name [routeType]="routeType()" />
      <ng-content />
    </div>
  `,
  styles: `
    .route-type {
      display: inline-flex;
      flex-direction: row;
      align-items: center;
    }

    nz-icon {
      height: 24px;
      margin-right: 10px;
    }
  `,
  imports: [RouteTypeNameComponent, NzIconDirective],
})
export class RouteTypeComponent {
  routeType = input.required<RouteType>();
}
