import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit, Component, Input } from '@angular/core';
import { PoiDetail } from '@app/kpn/api/common/poi-detail';
import { MAP_SERVICE_TOKEN } from '../services/openlayers-map-service';
import { PoiDetailMapService } from './poi-detail-map.service';

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
