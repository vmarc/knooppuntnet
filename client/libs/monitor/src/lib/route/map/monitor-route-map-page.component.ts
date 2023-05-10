import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteMapPage } from '@api/common/monitor';
import { Store } from '@ngrx/store';
import { MonitorRoutePageHeaderComponent } from '../components/monitor-route-page-header.component';
import { MonitorRouteMapComponent } from './monitor-route-map.component';
import { actionMonitorRouteMapPageDestroy } from './store/monitor-route-map.actions';
import { actionMonitorRouteMapPageInit } from './store/monitor-route-map.actions';
import { selectMonitorRouteMapPage } from './store/monitor-route-map.selectors';

@Component({
  selector: 'kpn-monitor-route-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-monitor-route-page-header pageName="map" />
    <div *ngIf="mapPage() as page">
      <div *ngIf="canDisplayMap(page); then map; else noMap"></div>
      <ng-template #noMap>
        <p i18n="@@monitor.route.map.no-map">No map</p>
      </ng-template>
      <ng-template #map>
        <kpn-monitor-route-map />
      </ng-template>
    </div>
  `,
  standalone: true,
  imports: [
    MonitorRoutePageHeaderComponent,
    NgIf,
    MonitorRouteMapComponent,
    AsyncPipe,
  ],
})
export class MonitorRouteMapPageComponent implements OnInit, OnDestroy {
  readonly mapPage = this.store.selectSignal(selectMonitorRouteMapPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorRouteMapPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionMonitorRouteMapPageDestroy());
  }

  canDisplayMap(page: MonitorRouteMapPage): boolean {
    return (
      !!page &&
      (page.bounds.minLat !== 0 ||
        page.bounds.minLon !== 0 ||
        page.bounds.maxLat !== 0 ||
        page.bounds.maxLon !== 0)
    );
  }
}
