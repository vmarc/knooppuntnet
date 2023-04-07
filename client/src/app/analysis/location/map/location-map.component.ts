import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { Bounds } from '@api/common/bounds';
import { NetworkType } from '@api/custom/network-type';
import { MapService } from '@app/components/ol/services/map.service';
import { Store } from '@ngrx/store';
import { actionLocationMapViewInit } from '@app/analysis/location/store/location.actions';
import { LocationMapService } from '@app/analysis/location/map/location-map.service';
import { MAP_SERVICE_TOKEN } from '@app/components/ol/services/openlayers-map-service';

@Component({
  selector: 'kpn-location-map',
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
      useExisting: LocationMapService,
    },
  ],
})
export class LocationMapComponent implements AfterViewInit, OnDestroy {
  @Input() networkType: NetworkType;
  @Input() bounds: Bounds;
  @Input() geoJson: string;

  constructor(
    protected service: LocationMapService,
    private mapService: MapService,
    private store: Store
  ) {}

  ngAfterViewInit(): void {
    this.store.dispatch(actionLocationMapViewInit());
    this.mapService.nextNetworkType(this.networkType); // TODO can do better?
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
