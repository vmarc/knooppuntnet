import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { PoiDetail } from '@api/common';
import { MAP_SERVICE_TOKEN } from '../services';
import { MapLinkMenuComponent } from './map-link-menu.component';
import { PoiDetailMapService } from './poi-detail-map.service';

@Component({
  selector: 'kpn-poi-detail-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="service.mapId" class="kpn-embedded-map">
      <kpn-map-link-menu />
    </div>
  `,
  providers: [
    PoiDetailMapService,
    {
      provide: MAP_SERVICE_TOKEN,
      useExisting: PoiDetailMapService,
    },
  ],
  imports: [MapLinkMenuComponent],
})
export class PoiDetailMapComponent implements AfterViewInit, OnDestroy {
  poiDetail = input.required<PoiDetail>();

  readonly service = inject(PoiDetailMapService);

  ngAfterViewInit(): void {
    this.service.init(this.poiDetail());
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
