import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RouteDetailsPage } from '@api/common/route/route-details-page';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/shared/services/api.service';
import { Subscriptions } from '@app/util';
import { FocusElements } from '../../../../map/focus-elements';
import { MapService } from '../../../../map/map.service';
import { RouterService } from '@app/shared/services/router.service';
import { RouteService } from '../route.service';

@Injectable()
export class RouteDetailsPageService {
  private readonly apiService = inject(ApiService);
  private readonly routeService = inject(RouteService);
  private readonly routerService = inject(RouterService);
  private readonly mapService = inject(MapService);
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly subscriptions = new Subscriptions();

  private readonly _response = signal<ApiResponse<RouteDetailsPage>>(null);
  readonly response = this._response.asReadonly();

  onInit(): void {
    this.subscriptions.add(
      this.activatedRoute.params.subscribe((params) => {
        this.routeService.initPage(this.routerService);
        this.load();
      })
    );
  }

  private load(): void {
    this.apiService.routeDetails(this.routeService.routeId()).subscribe((response) => {
      if (response.result) {
        const name = response.result.route.summary.name;
        const routeType = response.result.route.summary.routeTypes[0]; // TODO redesign
        const changeCount = response.result.changeCount;
        this.routeService.updateRoute(routeType, name, changeCount);
      }
      this._response.set(response);
      const route = response.result?.route;

      if (route) {
        const routeIds = route.routeIds.map((id) => id.toString());
        const nodeIds = new Array<string>();
        if (route.nodes.startNode) {
          nodeIds.push(route.nodes.startNode.nodeId.toString());
        }
        if (route.nodes.endNode) {
          nodeIds.push(route.nodes.endNode.nodeId.toString());
        }
        route.nodes.startTentacleNodes
          .map((node) => node.nodeId.toString())
          .forEach((nodeId) => nodeIds.push(nodeId));
        route.nodes.endTentacleNodes
          .map((node) => node.nodeId.toString())
          .forEach((nodeId) => nodeIds.push(nodeId));
        route.nodes.redundantNodes
          .map((node) => node.nodeId.toString())
          .forEach((nodeId) => nodeIds.push(nodeId));
        const elements: FocusElements = {
          nodeIds,
          routeIds,
        };
        this.mapService.focusElements(route.bounds, elements);
      }
    });
  }
}
