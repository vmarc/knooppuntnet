import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RouteDetailsPage } from '@api/common/route/route-details-page';
import { RouteSegment } from '@api/common/route/route-segment';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/shared/services/api.service';
import { Subscriptions } from '@app/util/subscriptions';
import { FocusElements } from '@app/state/focus-elements';
import { MapService } from '@app/map/map.service';
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

  private readonly _selectedSegment = signal<RouteSegment>(null);
  readonly selectedSegment = this._selectedSegment.asReadonly();

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
        this.routeService.updateRoute(response.result.routeInfo);
      }
      this._response.set(response);
      const data = response.result?.data;

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
      this.mapService.focusElements(this.response().result.routeInfo.bounds, elements);
    }
    this._selectedSegment.set(routeSegment);
  }
}
