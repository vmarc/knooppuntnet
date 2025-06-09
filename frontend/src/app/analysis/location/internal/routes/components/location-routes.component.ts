import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { LocationRoutesPage } from '@api/common/location/location-routes-page';
import { LocationRouteListComponent } from './location-route-list.component';

@Component({
  selector: 'ui-location-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (page().routes.length === 0) {
      <div class="kpn-spacer-above" i18n="@@location-routes.no-routes">No routes</div>
    } @else {
      <ui-location-route-list
        [timeInfo]="page().timeInfo"
        [routes]="page().routes"
        [routeCount]="page().routeCount"
      />
    }
  `,
  imports: [LocationRouteListComponent],
})
export class LocationRoutesComponent {
  readonly page = input.required<LocationRoutesPage>();
}
