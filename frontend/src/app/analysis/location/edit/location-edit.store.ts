import { inject } from '@angular/core';
import { LocationEditPage } from '@api/common/location';
import { ApiResponse } from '@api/custom';
import { ApiService } from '@app/services';
import { withHooks } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { LocationStore } from '../location.store';

export interface LocationEditState {
  response: ApiResponse<LocationEditPage> | null;
}

const initialState: LocationEditState = {
  response: null,
};

export const LocationEditStore = signalStore(
  withState(initialState),
  withMethods((store, apiService = inject(ApiService), locationStore = inject(LocationStore)) => {
    const load = () => {
      apiService.locationEdit(locationStore.locationKey()).subscribe((response) => {
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
