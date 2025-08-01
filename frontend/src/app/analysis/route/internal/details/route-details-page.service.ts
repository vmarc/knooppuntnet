import { HttpResourceRef } from '@angular/common/http';
import { effect } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { RouteDetailsPage } from '@api/common/route/route-details-page';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/shared/services/api.service';
import { FocusElements } from '@app/state/focus-elements';
import { MapService } from '@app/map/map.service';
import { State } from '@app/state/state';
import { RouteService } from '../route.service';

@Injectable()
export class RouteDetailsPageService {
  private readonly state = inject(State);
  private readonly apiService = inject(ApiService);
  private readonly routeService = inject(RouteService);
  private readonly mapService = inject(MapService);

  readonly response: HttpResourceRef<ApiResponse<RouteDetailsPage>>;

  constructor() {
    this.response = this.routeService.request('details', () =>
      this.apiService.routeDetails(this.routeService.routeId())
    );
    effect(() => {
      if (this.response.hasValue()) {
        const details = this.response.value().result?.details;

        if (details) {
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
      }
    });
  }
}
