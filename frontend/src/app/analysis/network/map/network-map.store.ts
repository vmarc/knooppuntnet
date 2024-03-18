import { Signal } from '@angular/core';
import { inject } from '@angular/core';
import { NetworkMapPage } from '@api/common/network';
import { ApiResponse } from '@api/custom';
import { MapPosition } from '@app/ol/domain';
import { NetworkMapPosition } from '@app/ol/domain';
import { ApiService } from '@app/services';
import { withHooks } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { RouterService } from '../../../shared/services/router.service';
import { NetworkStore } from '../network.store';
import { NetworkMapService } from './components/network-map.service';

export type NetworkMapState = {
  response: ApiResponse<NetworkMapPage>;
  mapPositionFromUrl: NetworkMapPosition;
};

export const initialState: NetworkMapState = {
  response: null,
  mapPositionFromUrl: null,
};

export const NetworkMapStore = signalStore(
  withState(initialState),
  withMethods((store) => {
    const apiService = inject(ApiService);
    const networkStore = inject(NetworkStore);
    const networkMapService = inject(NetworkMapService);
    const routerService = inject(RouterService);

    const load = () => {
      apiService.networkMap(networkStore.networkId()).subscribe((response) => {
        if (response.result) {
          networkStore.updateSummary(response.result.summary);
        }
        patchState(store, {
          response,
        });
      });
    };

    return {
      init: () => {
        networkStore.initPage();
        load();
      },
      afterViewInit: () => {
        let mapPositionFromUrl: NetworkMapPosition = undefined;
        const mapPositionString = routerService.queryParam('position');
        if (mapPositionString) {
          const mapPosition = MapPosition.fromQueryParam(mapPositionString);
          if (mapPosition) {
            mapPositionFromUrl = mapPosition.toNetworkMapPosition(
              mapPosition,
              networkStore.networkId()
            );
          }
        }

        networkMapService.init(
          networkStore.networkId(),
          store.response().result,
          mapPositionFromUrl
        );
      },
      destroy: (): void => {
        networkMapService.destroy();
      },
      networkId: (): Signal<number> => {
        return networkStore.networkId;
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
