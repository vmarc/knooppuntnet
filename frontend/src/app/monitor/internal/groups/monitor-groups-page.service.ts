import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorGroupsPageGroup } from '@api/common/monitor/monitor-groups-page-group';
import { MapService } from '@app/map/map.service';
import { State } from '@app/state/state';
import { MonitorService } from '../monitor.service';
import { initialState } from './monitor-groups-page.state';
import { MonitorGroupsPageState } from './monitor-groups-page.state';

@Injectable()
export class MonitorGroupsPageService {
  private readonly state = inject(State);
  private readonly mapService = inject(MapService);
  private readonly monitorService = inject(MonitorService);

  private readonly _pageState = signal<MonitorGroupsPageState>(initialState);
  readonly pageState = this._pageState.asReadonly();
  readonly admin = this.monitorService.admin;

  constructor() {
    this.monitorService
      .groups()
      .subscribe((response) => this._pageState.update((state) => ({ ...state, response })));
  }

  selectGroup(group: MonitorGroupsPageGroup): void {
    this.state.map.updateMode('monitor');
    this.state.map.updateMonitorRouteIds(group.monitorRouteIds);
    this.state.map.updateMonitorRelationIds([]);
    this.mapService.fitBounds(group.bounds);
  }
}
