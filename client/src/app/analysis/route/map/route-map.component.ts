import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { AfterViewInit, Component } from '@angular/core';
import { RouteMapService } from '@app/analysis/route/map/route-map.service';
import { actionRouteMapViewInit } from '@app/analysis/route/store/route.actions';
import { Store } from '@ngrx/store';
import { MAP_SERVICE_TOKEN } from '@app/components/ol/services/openlayers-map-service';

@Component({
  selector: 'kpn-route-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="service.mapId" class="kpn-map">
      <kpn-layer-switcher />
      <kpn-map-link-menu />
    </div>
  `,
  providers: [
    {
      provide: MAP_SERVICE_TOKEN,
      useExisting: RouteMapService,
    },
  ],
})
export class RouteMapComponent implements AfterViewInit, OnDestroy {
  constructor(protected service: RouteMapService, private store: Store) {}

  ngAfterViewInit(): void {
    this.store.dispatch(actionRouteMapViewInit());
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
