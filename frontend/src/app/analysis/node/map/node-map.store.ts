import { inject } from '@angular/core';
import { NodeMapPage } from '@api/common/node';
import { NetworkType } from '@api/custom';
import { ApiResponse } from '@api/custom';
import { MapPosition } from '@app/ol/domain';
import { ApiService } from '@app/services';
import { withHooks } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { RouterService } from '../../../shared/services/router.service';
import { NodeStore } from '../node.store';
import { NodeMapService } from './components/node-map.service';

export type NodeMapState = {
  response: ApiResponse<NodeMapPage>;
  mapPositionFromUrl: MapPosition;
};

export const initialState: NodeMapState = {
  response: null,
  mapPositionFromUrl: null,
};

export const NodeMapStore = signalStore(
  withState(initialState),
  withMethods((store) => {
    const apiService = inject(ApiService);
    const nodeStore = inject(NodeStore);
    const nodeMapService = inject(NodeMapService);
    const routerService = inject(RouterService);

    const load = () => {
      apiService.nodeMap(nodeStore.nodeId()).subscribe((response) => {
        if (response.result) {
          nodeStore.updateNode(response.result.nodeMapInfo.name, response.result.changeCount);
        }
        patchState(store, {
          response,
        });
      });
    };

    return {
      init: () => {
        nodeStore.initPage();
        load();
      },
      afterViewInit: () => {
        const mapPositionString = routerService.queryParam('position');
        const mapPositionFromUrl = MapPosition.fromQueryParam(mapPositionString);

        nodeMapService.init(
          store.response().result.nodeMapInfo,
          NetworkType.hiking, // TODO get preferred networkType from preferencesStore
          mapPositionFromUrl
        );
      },
      destroy: (): void => {
        nodeMapService.destroy();
      },
    };
  }),
  withHooks({
    onInit({ init }): void {
      init();
    },
    onDestroy({ destroy }): void {
      destroy();
    },
  })
);
