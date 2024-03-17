import { inject } from '@angular/core';
import { LocationFactsPage } from '@api/common/location';
import { ApiResponse } from '@api/custom';
import { ApiService } from '@app/services';
import { withHooks } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { LocationStore } from '../location.store';

export interface LocationFactsState {
  response: ApiResponse<LocationFactsPage> | null;
}

const initialState: LocationFactsState = {
  response: null,
};

export const LocationFactsStore = signalStore(
  withState(initialState),
  withMethods((store) => {
    const apiService = inject(ApiService);
    const locationStore = inject(LocationStore);

    const load = () => {
      apiService.locationFacts(locationStore.locationKey()).subscribe((response) => {
        if (response.result) {
          locationStore.updateSummary(response.result.summary);
        }
        patchState(store, {
          response,
        });
      });
    };

    return {
      init: () => {
        locationStore.initLocationPage();
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
