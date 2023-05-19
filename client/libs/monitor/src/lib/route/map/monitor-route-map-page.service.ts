import { Injectable } from '@angular/core';
import { MonitorRouteMapPage } from '@api/common/monitor';
import { Util } from '@app/components/shared';
import { NavService } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';
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
          console.log('MonitorRouteMapPageService processing api response');
          if (response.result) {
            const page = response.result;
            if (page) {
              let relationId = 0;
              if (page.currentSubRelation) {
                relationId = page.currentSubRelation.relationId;
              } else if (page.relationId) {
                relationId = page.relationId;
              }
              console.log('MonitorRouteMapPageService caching result');
              this.pages.set(relationId, page);
              console.log('MonitorRouteMapPageService pageChanged start');
              this.mapService.pageChanged(page);
              console.log('MonitorRouteMapPageService pageChanged done');
            }
          }
        });
    }
  }
}
