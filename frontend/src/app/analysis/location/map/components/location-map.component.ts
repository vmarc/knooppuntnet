import { OnDestroy } from '@angular/core';
import { inject } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Util } from '@app/components/shared';
import { MapLinkMenuComponent } from '@app/ol/components';
import { LayerSwitcherComponent } from '@app/ol/components';
import { MAP_SERVICE_TOKEN } from '@app/ol/services';
import { LocationMapPageService } from '../location-map-page.service';
import { LocationMapControlComponent } from './location-map-control';
import { LocationMapService } from './location-map.service';

@Component({
  selector: 'kpn-location-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="mapService.mapId" class="kpn-map">
      <kpn-location-map-control (action)="zoomToLocation()" />
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
  imports: [LayerSwitcherComponent, LocationMapControlComponent, MapLinkMenuComponent],
})
export class LocationMapComponent implements AfterViewInit, OnDestroy {
  private readonly service = inject(LocationMapPageService);
  protected readonly mapService = inject(LocationMapService);
  private readonly bounds = this.service.bounds;

  ngAfterViewInit(): void {
    this.service.afterViewInit();
  }

  ngOnDestroy(): void {
    this.mapService.destroy();
  }

  zoomToLocation(): void {
    if (this.bounds()) {
      const extent = Util.toExtent(this.bounds(), 0.1);
      this.mapService.map.getView().fit(extent);
    }
  }
}
