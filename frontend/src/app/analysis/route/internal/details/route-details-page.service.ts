import { HttpResourceRef } from '@angular/common/http';
import { effect } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { RouteDetailsPage } from '@api/common/route/route-details-page';
import { RouteSegment } from '@api/common/route/route-segment';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/shared/services/api.service';
import { FocusElements } from '@app/state/focus-elements';
import { MapService } from '@app/map/map.service';
import { RouteService } from '../route.service';

@Injectable()
export class RouteDetailsPageService {
  private readonly apiService = inject(ApiService);
  private readonly routeService = inject(RouteService);
  private readonly mapService = inject(MapService);

  readonly response: HttpResourceRef<ApiResponse<RouteDetailsPage>>;

  private readonly _selectedSegment = signal<RouteSegment>(null);
  readonly selectedSegment = this._selectedSegment.asReadonly();

  constructor() {
    this.response = this.routeService.request('details', () =>
      this.apiService.routeDetails(this.routeService.routeId())
    );
    effect(() => {
      if (this.response.hasValue()) {
        const data = this.response.value().result?.data;

        if (data) {
          const routeIds = data.routeIds.map((id) => id.toString());
          const nodeIds = new Array<string>();
          if (data.nodes.startNode) {
            nodeIds.push(data.nodes.startNode.nodeId.toString());
          }
          if (data.nodes.endNode) {
            nodeIds.push(data.nodes.endNode.nodeId.toString());
          }
          data.nodes.startTentacleNodes
            .map((node) => node.nodeId.toString())
            .forEach((nodeId) => nodeIds.push(nodeId));
          data.nodes.endTentacleNodes
            .map((node) => node.nodeId.toString())
            .forEach((nodeId) => nodeIds.push(nodeId));
          data.nodes.redundantNodes
            .map((node) => node.nodeId.toString())
            .forEach((nodeId) => nodeIds.push(nodeId));
          const elements: FocusElements = {
            nodeIds,
            routeIds,
          };
          this.mapService.updateSelectedRoute(this.routeService.routeId());
          this.mapService.focusElements(data.bounds, elements);
        }
      }
    });
  }

  selectSegment(routeSegment: RouteSegment): void {
    this.mapService.setMapMode('route-segments');
    const elements: FocusElements = {
      nodeIds: [],
      routeIds: [],
    };
    if (routeSegment) {
      this.mapService.focusElements(routeSegment.bounds, elements);
    } else {
      this.mapService.focusElements(this.response.value().result.routeInfo.bounds, elements);
    }
    this._selectedSegment.set(routeSegment);
  }
}
