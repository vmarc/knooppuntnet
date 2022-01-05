import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { LocationRoutesPage } from '@api/common/location/location-routes-page';

@Component({
  selector: 'kpn-location-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div
      *ngIf="page.routes.length === 0"
      class="kpn-spacer-above"
      i18n="@@location-routes.no-routes"
    >
      No routes
    </div>
    <kpn-location-route-table
      *ngIf="page.routes.length > 0"
      [timeInfo]="page.timeInfo"
      [routes]="page.routes"
      [routeCount]="page.routeCount"
    >
    </kpn-location-route-table>
  `,
})
export class LocationRoutesComponent {
  @Input() page: LocationRoutesPage;
}
