import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MAP_SERVICE_TOKEN } from '@app/components/ol/services';
import { Store } from '@ngrx/store';
import { MonitorRouteMapService } from './monitor-route-map.service';
import { actionMonitorRouteMapPageViewInit } from './store/monitor-route-map.actions';
import { actionMonitorRouteMapPageDestroy } from './store/monitor-route-map.actions';

@Component({
  selector: 'kpn-monitor-route-map',
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
      useExisting: MonitorRouteMapService,
    },
  ],
})
export class MonitorRouteMapComponent implements AfterViewInit, OnDestroy {
  constructor(
    protected service: MonitorRouteMapService,
    private store: Store
  ) {}

  ngAfterViewInit(): void {
    this.store.dispatch(actionMonitorRouteMapPageViewInit());
  }

  ngOnDestroy(): void {
    this.service.destroy();
    this.store.dispatch(actionMonitorRouteMapPageDestroy());
  }
}
