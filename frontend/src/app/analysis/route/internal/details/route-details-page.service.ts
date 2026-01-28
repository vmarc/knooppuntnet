import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { RouteDetailsPage } from '@api/common/route/route-details-page';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/shared/services/api.service';
import { RouterService } from '@app/shared/services/router.service';
import { FocusElements } from '@app/state/focus-elements';
import { OldMapService } from '@app/mapold/old-map.service';
import { State } from '@app/state/state';
import { RouteService } from '../route.service';

@Injectable()
export class RouteDetailsPageService {
  private readonly state = inject(State);
  private readonly apiService = inject(ApiService);
  private readonly routeService = inject(RouteService);
  private readonly routerService = inject(RouterService);
  private readonly mapService = inject(OldMapService);

  private readonly _response = signal<ApiResponse<RouteDetailsPage>>(null);
  readonly response = this._response.asReadonly();

  onInit() {
    const routeId = +this.routerService.param('routeId');
    this.routeService.onInit('details', routeId);
    this.apiService.routeDetails(routeId).subscribe((response) => {
      this._response.set(response);
      if (response.result?.details) {
        this.routeService.updateRoute(response.result.routeInfo);
        const details = response.result.details;

        const routeIds = details.routeIds.map((id) => id.toString());
        const nodeIds = new Array<string>();
        if (details.nodes.startNode) {
          nodeIds.push(details.nodes.startNode.nodeId.toString());
        }
        if (details.nodes.endNode) {
          nodeIds.push(details.nodes.endNode.nodeId.toString());
        }
        details.nodes.startTentacleNodes
          .map((node) => node.nodeId.toString())
          .forEach((nodeId) => nodeIds.push(nodeId));
        details.nodes.endTentacleNodes
          .map((node) => node.nodeId.toString())
          .forEach((nodeId) => nodeIds.push(nodeId));
        details.nodes.redundantNodes
          .map((node) => node.nodeId.toString())
          .forEach((nodeId) => nodeIds.push(nodeId));
        const elements: FocusElements = {
          nodeIds,
          routeIds,
        };

        this.state.routeDetailsPageOpened(this.routeService.routeId());
        this.state.map.updateFocusElements(elements);
        this.mapService.fitBounds(this.routeService.bounds());
      }
    });
  }
}
