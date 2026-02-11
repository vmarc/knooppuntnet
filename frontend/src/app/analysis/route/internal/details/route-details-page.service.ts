import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { RouteDetailsPage } from '@api/common/route/route-details-page';
import { RouteNode } from '@api/common/route/route-node';
import { ApiResponse } from '@api/custom/api-response';
import { MapService } from '@app/map/map.service';
import { ApiService } from '@app/shared/services/api.service';
import { RouterService } from '@app/shared/services/router.service';
import { FocusElements } from '@app/state/focus-elements';
import { OldMapService } from '@app/mapold/old-map.service';
import { State } from '@app/state/state';
import { Marker } from 'maplibre-gl';
import { RouteService } from '../route.service';

@Injectable()
export class RouteDetailsPageService {
  private readonly state = inject(State);
  private readonly apiService = inject(ApiService);
  private readonly routeService = inject(RouteService);
  private readonly routerService = inject(RouterService);
  private readonly mapService = inject(MapService);

  private readonly _response = signal<ApiResponse<RouteDetailsPage>>(null);
  readonly response = this._response.asReadonly();

  private markers: Marker[] = [];

  onInit() {
    const routeId = +this.routerService.param('routeId');
    this.routeService.onInit('details', routeId);
    this.apiService.routeDetails(routeId).subscribe((response) => {
      this._response.set(response);
      if (response.result?.details) {
        this.routeService.updateRoute(response.result.routeInfo);
        const details = response.result.details;

        const nodeIds = new Array<number>();
        if (details.nodes.startNode) {
          nodeIds.push(details.nodes.startNode.nodeId);
        }
        if (details.nodes.endNode) {
          nodeIds.push(details.nodes.endNode.nodeId);
        }
        details.nodes.startTentacleNodes
          .map((node) => node.nodeId)
          .forEach((nodeId) => nodeIds.push(nodeId));
        details.nodes.endTentacleNodes
          .map((node) => node.nodeId)
          .forEach((nodeId) => nodeIds.push(nodeId));
        details.nodes.redundantNodes
          .map((node) => node.nodeId)
          .forEach((nodeId) => nodeIds.push(nodeId));

        this.state.routeDetailsPageOpened(this.routeService.routeId());
        this.state.map.updateNodeRoutes(details.routeIds, nodeIds);

        if (details.nodes.startNode) {
          this.addMarker(details.nodes.startNode, '#00ff00');
        }
        if (details.nodes.endNode) {
          this.addMarker(details.nodes.endNode, '#ff0000');
        }
        details.nodes.startTentacleNodes.forEach((node) => this.addMarker(node, '#ffa500'));
        details.nodes.endTentacleNodes.forEach((node) => this.addMarker(node, '#b200ed'));
        details.nodes.redundantNodes.forEach((node) => this.addMarker(node, '#ffff00'));

        this.markers.forEach((marker) => this.mapService.addMarker(marker));

        this.mapService.execute(() => {
          this.state.map.updateMode('route-details');
        });

        this.mapService.fitBounds(this.routeService.bounds());
      }
    });
  }
  onDestroy(): void {
    this.markers.forEach((marker) => marker.remove());
  }

  private addMarker(node: RouteNode, color: string): void {
    const marker = new Marker({
      color: color,
    });
    marker.setLngLat({ lon: +node.longitude, lat: +node.latitude });
    this.markers.push(marker);
  }
}
