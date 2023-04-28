import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { PoiDetail } from '@api/common';
import { MAP_SERVICE_TOKEN } from '../services';
import { PoiDetailMapService } from './poi-detail-map.service';
import { MapLinkMenuComponent } from './map-link-menu.component';
import { LayerSwitcherComponent } from './layer-switcher.component';

@Component({
  selector: 'kpn-poi-detail-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="service.mapId" class="kpn-embedded-map">
      <kpn-layer-switcher />
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
  standalone: true,
  imports: [LayerSwitcherComponent, MapLinkMenuComponent],
})
export class PoiDetailMapComponent implements AfterViewInit, OnDestroy {
  @Input() poiDetail: PoiDetail;

  constructor(protected service: PoiDetailMapService) {}

  ngAfterViewInit(): void {
    this.service.init(this.poiDetail);
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
