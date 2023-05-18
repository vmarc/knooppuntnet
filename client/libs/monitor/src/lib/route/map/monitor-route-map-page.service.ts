import { effect } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorRouteMapPage } from '@api/common/monitor';
import { Util } from '@app/components/shared';
import { NavService } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';
import { MonitorRouteMapService } from './monitor-route-map.service';

@Injectable()
export class MonitorRouteMapPageService {
  private pages: Map<number, MonitorRouteMapPage> = new Map();
  private readonly apiResponse = Util.response<MonitorRouteMapPage>();
  private readonly _routeDescription = this.nav.state('description');

  readonly groupName = this.nav.param('groupName');
  readonly routeName = this.nav.param('routeName');
  readonly subRelationId = this.nav.queryParam('sub-relation-id');
  readonly routeDescription = this._routeDescription.asReadonly();

  constructor(
    private monitorService: MonitorService,
    private mapService: MonitorRouteMapService,
    private nav: NavService
  ) {
    effect(() => {
      const page = this.apiResponse()?.result;
      if (page) {
        this.pages.set(page.currentSubRelation.relationId, page);
        this.mapService.pageChanged(page);
      }
    });
  }

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
            let relationId = 0;
            if (response.result.currentSubRelation) {
              relationId = response.result.currentSubRelation.relationId;
            } else if (response.result.relationId) {
              relationId = response.result.relationId;
            }
            this.pages.set(relationId, response.result);
            this.mapService.pageChanged(response.result);
          }
        });
    }
  }
}
