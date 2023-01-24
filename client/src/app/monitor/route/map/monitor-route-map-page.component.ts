import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteMapPage } from '@api/common/monitor/monitor-route-map-page';
import { AppState } from '@app/core/core.state';
import { Store } from '@ngrx/store';
import { actionMonitorRouteMapPageDestroy } from '../../store/monitor.actions';
import { actionMonitorRouteMapPageInit } from '../../store/monitor.actions';
import { selectMonitorRouteMapPage } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-monitor-route-page-header pageName="map" />
    <div *ngIf="response$ | async as response">
      <div *ngIf="canDisplayMap(response.result); then map; else noMap"></div>
      <ng-template #noMap>
        <p i18n="@@monitor.route.map.no-map">No map</p>
      </ng-template>
      <ng-template #map>
        <kpn-monitor-route-map [page]="response.result"/>
      </ng-template>
    </div>
  `,
})
export class MonitorRouteMapPageComponent implements OnInit, OnDestroy {
  readonly response$ = this.store.select(selectMonitorRouteMapPage);

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
