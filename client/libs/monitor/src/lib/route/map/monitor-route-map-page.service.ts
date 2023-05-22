import { Injectable } from '@angular/core';
import { MonitorRouteSubRelation } from '@api/common/monitor';
import { MonitorRouteMapPage } from '@api/common/monitor';
import { Util } from '@app/components/shared';
import { NavService } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';
import { MonitorRouteMapStateService } from './monitor-route-map-state.service';
import { MonitorRouteMapService } from './monitor-route-map.service';

@Injectable()
export class MonitorRouteMapPageService {
  private pages: Map<number, MonitorRouteMapPage> = new Map();
  private readonly _routeDescription = this.nav.state('description');

  readonly groupName = this.nav.param('groupName');
  readonly routeName = this.nav.param('routeName');
  readonly subRelationId = this.nav.queryParam('sub-relation-id');
  readonly routeDescription = this._routeDescription.asReadonly();

  constructor(
    private monitorService: MonitorService,
    private mapService: MonitorRouteMapService,
    private stateService: MonitorRouteMapStateService,
    private nav: NavService
  ) {}

  init(): void {
    const subRelationIdParameter = this.subRelationId();
    let relationId = 0;
    if (subRelationIdParameter) {
      relationId = Util.toInteger(subRelationIdParameter);
    }
    const routeMapPage = this.pages.get(relationId);
    if (routeMapPage) {
      this.mapService.pageChanged(routeMapPage);
    } else {
      this.monitorService
        .routeMap(this.groupName(), this.routeName(), relationId)
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
              this.pages.set(relationId, page);
              const params = this.nav.params();
              const queryParams = this.nav.queryParams();
              this.stateService.initialState(params, queryParams, page);
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
    } else {
      this.monitorService
        .routeMap(this.groupName(), this.routeName(), subRelation.relationId)
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
