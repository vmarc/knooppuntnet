import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { NodeMapPage } from '@api/common/node';
import { NetworkType } from '@api/custom';
import { ApiResponse } from '@api/custom';
import { MapPosition } from '@app/ol/domain';
import { ApiService } from '@app/services';
import { RouterService } from '../../../shared/services/router.service';
import { NodeService } from '../node.service';
import { NodeMapService } from './components/node-map.service';

@Injectable()
export class NodeMapPageService {
  private readonly apiService = inject(ApiService);
  private readonly nodeService = inject(NodeService);
  private readonly nodeMapService = inject(NodeMapService);
  private readonly routerService = inject(RouterService);

  private readonly _response = signal<ApiResponse<NodeMapPage>>(null);
  readonly response = this._response.asReadonly();

  onInit(): void {
    this.nodeService.initPage(this.routerService);
    this.load();
  }

  onAfterViewInit(): void {
    const mapPositionString = this.routerService.queryParam('position');
    const mapPositionFromUrl = MapPosition.fromQueryParam(mapPositionString);
    this.nodeMapService.init(
      this.response().result.nodeMapInfo,
      NetworkType.hiking, // TODO SIGNAL get preferred networkType from preferencesStore
      mapPositionFromUrl
    );
  }

  private load(): void {
    this.apiService.nodeMap(this.nodeService.nodeId()).subscribe((response) => {
      if (response.result) {
        this.nodeService.updateNode(response.result.nodeMapInfo.name, response.result.changeCount);
      }
      this._response.set(response);
    });
  }
}
