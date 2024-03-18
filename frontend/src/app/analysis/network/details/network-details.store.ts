import { inject } from '@angular/core';
import { NetworkDetailsPage } from '@api/common/network';
import { NetworkType } from '@api/custom';
import { ApiResponse } from '@api/custom';
import { ApiService } from '@app/services';
import { withHooks } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { PreferencesStore } from '../../../shared/core/preferences/preferences.store';
import { NetworkStore } from '../network.store';

export type NetworkDetailsState = {
  response: ApiResponse<NetworkDetailsPage>;
};

export const initialState: NetworkDetailsState = {
  response: null,
};

export const NetworkDetailsStore = signalStore(
  withState(initialState),
  withMethods((store) => {
    const apiService = inject(ApiService);
    const networkStore = inject(NetworkStore);
    const preferencesStore = inject(PreferencesStore);
    const load = (): void => {
      apiService.networkDetails(networkStore.networkId()).subscribe((response) => {
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
    };
  }),
  withHooks({
    onInit({ init }) {
      init();
    },
  })
);
