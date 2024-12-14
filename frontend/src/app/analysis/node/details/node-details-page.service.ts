import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { NodeInfo } from '@api/common';
import { NodeDetailsPage } from '@api/common/node';
import { ApiResponse } from '@api/custom';
import { NetworkTypes } from '@app/kpn/common';
import { ApiService } from '@app/services';
import { MapService } from '../../../map/map.service';
import { RouterService } from '../../../shared/services/router.service';
import { NodeService } from '../node.service';

@Injectable()
export class NodeDetailsPageService {
  private readonly apiService = inject(ApiService);
  private readonly nodeService = inject(NodeService);
  private readonly routerService = inject(RouterService);
  private readonly mapService = inject(MapService);

  private readonly _response = signal<ApiResponse<NodeDetailsPage>>(null);
  readonly response = this._response.asReadonly();

  readonly networkTypes = computed(() => {
    const resp = this.response();
    if (resp) {
      const networkTypes = resp.result.nodeInfo.names.map((nodeName) => nodeName.networkType);
      return NetworkTypes.all.filter((networkType) => networkTypes.includes(networkType));
    }
    return [];
  });

  onInit(): void {
    this.nodeService.initPage(this.routerService);
    this.load();
  }

  private load(): void {
    this.apiService.nodeDetails(this.nodeService.nodeId()).subscribe((response) => {
      if (response.result) {
        this.nodeService.updateNode(response.result.nodeInfo.name, response.result.changeCount);
        this.focusOnNode(response.result.nodeInfo);
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
