import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { PoiDetail } from '@api/common/poi-detail';
import { MAP_SERVICE_TOKEN } from '../services/openlayers-map-service';
import { PoiDetailMapService } from './poi-detail-map.service';

@Component({
  selector: 'ui-poi-detail-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <div [id]="service.mapId" class="kpn-embedded-map"></div> `,
  providers: [
    PoiDetailMapService,
    {
      provide: MAP_SERVICE_TOKEN,
      useExisting: PoiDetailMapService,
    },
  ],
})
export class PoiDetailMapComponent implements AfterViewInit, OnDestroy {
  readonly poiDetail = input.required<PoiDetail>();

  readonly service = inject(PoiDetailMapService);

  ngAfterViewInit(): void {
    this.service.init(this.poiDetail());
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
