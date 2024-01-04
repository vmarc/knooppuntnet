import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { LocationRoutesPage } from '@api/common/location';
import { LocationRouteTableComponent } from './location-route-table.component';

@Component({
  selector: 'kpn-location-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (page.routes.length === 0) {
      <div class="kpn-spacer-above" i18n="@@location-routes.no-routes">No routes</div>
    } @else {
      <kpn-location-route-table
        [timeInfo]="page.timeInfo"
        [routes]="page.routes"
        [routeCount]="page.routeCount"
      />
    }
  `,
  standalone: true,
  imports: [LocationRouteTableComponent],
})
export class LocationRoutesComponent {
  @Input() page: LocationRoutesPage;
}
