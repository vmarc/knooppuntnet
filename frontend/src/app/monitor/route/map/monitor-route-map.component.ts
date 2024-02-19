import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MapLinkMenuComponent } from '@app/ol/components';
import { LayerSwitcherComponent } from '@app/ol/components';
import { MAP_SERVICE_TOKEN } from '@app/ol/services';
import { Coordinate } from 'ol/coordinate';
import { GeolocationControlComponent } from '../../../planner/pages/planner/geolocation/geolocation-control.component';
import { MonitorRouteMapService } from './monitor-route-map.service';

@Component({
  selector: 'kpn-monitor-route-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="mapId" class="kpn-map">
      <kpn-geolocation-control (action)="geolocation($event)" />
      <kpn-layer-switcher />
      <kpn-map-link-menu />
    </div>
  `,
  providers: [
    {
      provide: MAP_SERVICE_TOKEN,
      useExisting: MonitorRouteMapService,
    },
  ],
  standalone: true,
  imports: [LayerSwitcherComponent, MapLinkMenuComponent, GeolocationControlComponent],
})
export class MonitorRouteMapComponent implements AfterViewInit, OnDestroy {
  private readonly service = inject(MonitorRouteMapService);
  protected readonly mapId = this.service.mapId;

  ngAfterViewInit(): void {
    this.service.init();
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }

  geolocation(coordinate: Coordinate): void {
    this.service.map.getView().setCenter(coordinate);
    this.service.map.getView().setZoom(15);
  }
}
