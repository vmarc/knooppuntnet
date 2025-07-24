import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { NodeInfo } from '@api/common/node-info';
import { NodeDetailsPage } from '@api/common/node/node-details-page';
import { ApiResponse } from '@api/custom/api-response';
import { RouteTypes } from '@app/shared/kpn/common/route-types';
import { ApiService } from '@app/shared/services/api.service';
import { MapService } from '@app/map/map.service';
import { NodeService } from '../node.service';

@Injectable()
export class NodeDetailsPageService {
  private readonly apiService = inject(ApiService);
  private readonly nodeService = inject(NodeService);
  private readonly mapService = inject(MapService);

  private readonly _response = signal<ApiResponse<NodeDetailsPage>>(null);
  readonly response = this._response.asReadonly();

  readonly routeTypes = computed(() => {
    const resp = this.response();
    if (resp) {
      const routeTypes = resp.result.nodeInfo.names.map((nodeName) => nodeName.routeType);
      return RouteTypes.all.filter((routeType) => routeTypes.includes(routeType));
    }
    return [];
  });

  onInit(): void {
    this.nodeService.updatePageName('details');
    this.nodeService.updateNodeNotFound(false);
    this.apiService.nodeDetails(this.nodeService.nodeId()).subscribe((response) => {
      if (response.result) {
        this.nodeService.updateNode(response.result.nodeInfo.name, response.result.changeCount);
        this.focusOnNode(response.result.nodeInfo);
      } else {
        this.nodeService.updateNodeNotFound(true);
      }
      this._response.set(response);
    });
  }

  private focusOnNode(nodeInfo: NodeInfo): void {
    this.mapService.focusNode(
      {
        latitude: nodeInfo.latitude,
        longitude: nodeInfo.longitude,
      },
      nodeInfo.id.toString()
    );
  }
}
