import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { Component } from '@angular/core';
import { MAP_SERVICE_TOKEN } from '@app/ol/services/openlayers-map-service';
import { PoiAreasPageService } from '../poi-areas-page.service';
import { PoiMapService } from './poi-map.service';

@Component({
  selector: 'ui-poi-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <div [id]="service.mapId" class="kpn-map"></div> `,
  providers: [
    {
      provide: MAP_SERVICE_TOKEN,
      useExisting: PoiMapService,
    },
  ],
})
export class PoiMapComponent implements AfterViewInit, OnDestroy {
  readonly service = inject(PoiMapService);
  private readonly pageService = inject(PoiAreasPageService);

  ngAfterViewInit(): void {
    this.pageService.afterViewInit();
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
