import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MonitorRouteDeviation } from '@api/common/monitor';
import { MonitorRouteSegment } from '@api/common/monitor';
import { MapLinkMenuComponent } from '@app/ol/components';
import { LayerSwitcherComponent } from '@app/ol/components';
import { MAP_SERVICE_TOKEN } from '@app/ol/services';
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
  standalone: true,
  imports: [LayerSwitcherComponent, MapLinkMenuComponent],
})
export class MonitorRouteChangeMapComponent implements AfterViewInit, OnDestroy {
  referenceJson = input.required<string>();
  routeSegments = input.required<MonitorRouteSegment[]>();
  deviation = input.required<MonitorRouteDeviation>();

  protected readonly service = inject(MonitorRouteChangeMapService);

  ngAfterViewInit(): void {
    this.service.init(this.referenceJson(), this.deviation(), this.routeSegments());
  }

  ngOnDestroy(): void {
    this.service.destroy();
  }
}
