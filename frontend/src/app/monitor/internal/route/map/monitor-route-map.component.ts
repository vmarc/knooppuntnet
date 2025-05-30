import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MAP_SERVICE_TOKEN } from '@app/ol/services/openlayers-map-service';
import { GeolocationButtonComponent } from '@app/planner/pages/planner/geolocation/geolocation-button.component';
import { RouterService } from '@app/shared/services/router.service';
import { MonitorRouteMapService } from './monitor-route-map.service';

@Component({
  selector: 'ui-monitor-route-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="mapId" class="kpn-map">
      <ui-geolocation-button />
    </div>
  `,
  providers: [
    {
      provide: MAP_SERVICE_TOKEN,
      useExisting: MonitorRouteMapService,
    },
  ],
  imports: [GeolocationButtonComponent],
})
export class MonitorRouteMapComponent implements AfterViewInit, OnDestroy {
  private readonly service = inject(MonitorRouteMapService);
  private readonly routerService = inject(RouterService);
  readonly mapId = this.service.mapId;

  ngAfterViewInit(): void {
    this.service.init(this.routerService.urlLayerIds());
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
