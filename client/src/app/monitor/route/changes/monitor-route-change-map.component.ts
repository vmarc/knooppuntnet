import { Input } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteDeviation } from '@api/common/monitor/monitor-route-deviation';
import { MonitorRouteSegment } from '@api/common/monitor/monitor-route-segment';
import { MAP_SERVICE_TOKEN } from '../../../components/ol/services/openlayers-map-service';
import { MonitorRouteChangeMapService } from './monitor-route-change-map.service';

@Component({
  selector: 'kpn-monitor-route-change-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="service.mapId" class="kpn-embedded-map">
      <kpn-layer-switcher />
      <kpn-map-link-menu />
    </div>
  `,
  providers: [
    MonitorRouteChangeMapService,
    {
      provide: MAP_SERVICE_TOKEN,
      useExisting: MonitorRouteChangeMapService,
    },
  ],
})
export class MonitorRouteChangeMapComponent
  implements AfterViewInit, OnDestroy
{
  @Input() referenceJson: string;
  @Input() routeSegments: MonitorRouteSegment[];
  @Input() deviation: MonitorRouteDeviation;

  constructor(protected service: MonitorRouteChangeMapService) {}

  ngAfterViewInit(): void {
    this.service.init(this.referenceJson, this.deviation, this.routeSegments);
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
