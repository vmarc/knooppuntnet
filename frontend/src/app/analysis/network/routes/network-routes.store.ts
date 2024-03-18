import { Signal } from '@angular/core';
import { inject } from '@angular/core';
import { NetworkRoutesPage } from '@api/common/network';
import { ApiResponse } from '@api/custom';
import { ApiService } from '@app/services';
import { withHooks } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { PreferencesStore } from '../../../shared/core/preferences/preferences.store';
import { NetworkStore } from '../network.store';

export type NetworkRoutesState = {
  response: ApiResponse<NetworkRoutesPage>;
};

export const initialState: NetworkRoutesState = {
  response: null,
};

export const NetworkRoutesStore = signalStore(
  withState(initialState),
  withMethods((store) => {
    const apiService = inject(ApiService);
    const networkStore = inject(NetworkStore);
    const preferencesStore = inject(PreferencesStore);
    const load = (): void => {
      apiService.networkRoutes(networkStore.networkId()).subscribe((response) => {
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
