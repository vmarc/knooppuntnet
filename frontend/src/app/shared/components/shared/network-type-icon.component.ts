import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouteType } from '@api/common';

@Component({
  selector: 'kpn-network-type-icon',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <mat-icon [svgIcon]="routeType()" /> `,
  imports: [MatIconModule],
})
export class RouteTypeIconComponent {
  routeType = input.required<RouteType>();
}
