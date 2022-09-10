import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteMapPage } from '@api/common/monitor/monitor-route-map-page';
import { AppState } from '@app/core/core.state';
import { Store } from '@ngrx/store';
import { actionMonitorRouteMapPageInit } from '../../store/monitor.actions';
import { selectMonitorRouteMapPage } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-monitor-route-page-header
      pageName="map"
    ></kpn-monitor-route-page-header>
    <div *ngIf="response$ | async as response">
      <div *ngIf="canDisplayMap(response.result); then map; else noMap"></div>
      <ng-template #noMap>
        <p i18n="@@monitor.route.map.no-map">No map</p>
      </ng-template>
      <ng-template #map>
        <kpn-monitor-route-map [page]="response.result"></kpn-monitor-route-map>
      </ng-template>
    </div>
  `,
})
export class MonitorRouteMapPageComponent implements OnInit {
  readonly response$ = this.store.select(selectMonitorRouteMapPage);

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorRouteMapPageInit());
  }

  canDisplayMap(page: MonitorRouteMapPage): boolean {
    return !!page && page.bounds.maxLat > 0;
  }
}
