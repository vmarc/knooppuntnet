import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorRouteSubRelation } from '@api/common/monitor';
import { MonitorRouteMapPage } from '@api/common/monitor';
import { Util } from '@app/components/shared';
import { NavService } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';
import { initialState } from './monitor-route-map-page.state';
import { MonitorRouteMapPageState } from './monitor-route-map-page.state';
import { MonitorRouteMapStateService } from './monitor-route-map-state.service';
import { MonitorRouteMapService } from './monitor-route-map.service';

@Injectable()
export class MonitorRouteMapPageService {
  private readonly monitorService = inject(MonitorService);
  private readonly mapService = inject(MonitorRouteMapService);
  private readonly stateService = inject(MonitorRouteMapStateService);
  private readonly navService = inject(NavService);

  private readonly pages: Map<number, MonitorRouteMapPage> = new Map();
  private readonly _state = signal<MonitorRouteMapPageState>(initialState);
  readonly state = this._state.asReadonly();

  constructor() {
    const groupName = this.navService.param('groupName');
    const routeName = this.navService.param('routeName');
    const relationIdParameter = this.navService.queryParam('sub-relation-id');
    let relationId = 0;
    if (relationIdParameter) {
      relationId = Util.toInteger(relationIdParameter);
    }
    const routeDescription = this.navService.state('description');
    this._state.update((state) => ({
      ...state,
      groupName,
      routeName,
      relationId,
      routeDescription,
    }));

    const routeMapPage = this.pages.get(relationId);
    if (routeMapPage) {
      this.mapService.pageChanged(routeMapPage);
    } else {
      this.monitorService
        .routeMap(groupName, routeName, relationId)
        .subscribe((response) => {
          if (response.result) {
            const page = response.result;
            if (page) {
              let relationId = 0;
              if (page.currentSubRelation) {
                relationId = page.currentSubRelation.relationId;
              } else if (page.relationId) {
                relationId = page.relationId;
              }
              this._state.update((state) => ({
                ...state,
                routeDescription: page.routeDescription,
              }));
              this.pages.set(relationId, page);
              const queryParams = this.navService.queryParams();
              this.stateService.initialState(queryParams, page);
              this.mapService.pageChanged(page);
            }
          }
        });
    }
  }

  selectSubRelation(subRelation: MonitorRouteSubRelation): void {
    const page = this.pages.get(subRelation.relationId);
    if (page) {
      this.stateService.pageChanged(page);
      this.mapService.pageChanged(page);
      this.stateService.focusChanged(page.bounds);
      this._state.update((state) => ({
        ...state,
        routeDescription: page.routeDescription,
      }));
    } else {
      this.monitorService
        .routeMap(
          this.state().groupName,
          this.state().routeName,
          subRelation.relationId
        )
        .subscribe((response) => {
          if (response.result) {
            const page = response.result;
            if (page) {
              let relationId = 0;
              if (page.currentSubRelation) {
                relationId = page.currentSubRelation.relationId;
              } else if (page.relationId) {
                relationId = page.relationId;
              }
              this._state.update((state) => ({
                ...state,
                routeDescription: page.routeDescription,
              }));
              this.pages.set(relationId, page);
              this.stateService.pageChanged(page);
              this.mapService.pageChanged(page);
              this.stateService.focusChanged(page.bounds);
            }
          }
        });
    }
  }
}
