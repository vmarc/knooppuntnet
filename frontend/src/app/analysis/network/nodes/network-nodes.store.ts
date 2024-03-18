import { Signal } from '@angular/core';
import { inject } from '@angular/core';
import { NetworkNodesPage } from '@api/common/network';
import { ApiResponse } from '@api/custom';
import { ApiService } from '@app/services';
import { withHooks } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { PreferencesStore } from '../../../shared/core/preferences/preferences.store';
import { NetworkStore } from '../network.store';

export type NetworkNodesState = {
  response: ApiResponse<NetworkNodesPage>;
};

export const initialState: NetworkNodesState = {
  response: null,
};

export const NetworkNodesStore = signalStore(
  withState(initialState),
  withMethods((store) => {
    const apiService = inject(ApiService);
    const networkStore = inject(NetworkStore);
    const preferencesStore = inject(PreferencesStore);
    const load = (): void => {
      apiService.networkNodes(networkStore.networkId()).subscribe((response) => {
        if (response.result) {
          networkStore.updateSummary(response.result.summary);
        }
        patchState(store, {
          response,
        });
      });
    };

    return {
      init: (): void => {
        networkStore.initPage();
        load();
      },
      pageSize: (): Signal<number> => {
        return preferencesStore.pageSize;
      },
      updatePageSize: (pageSize: number): void => {
        preferencesStore.updatePageSize(pageSize);
        load();
      },
    };
  }),
  withHooks({
    onInit({ init }) {
      init();
    },
  })
);
