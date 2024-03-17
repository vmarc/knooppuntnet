import { inject } from '@angular/core';
import { LocationChangesParameters } from '@api/common/location';
import { LocationChangesPage } from '@api/common/location';
import { ApiResponse } from '@api/custom';
import { ApiService } from '@app/services';
import { withHooks } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { LocationStore } from '../location.store';

export interface LocationChangesState {
  response: ApiResponse<LocationChangesPage> | null;
}

const initialState: LocationChangesState = {
  response: null,
};

export const LocationChangesStore = signalStore(
  withState(initialState),
  withMethods((store, apiService = inject(ApiService), locationStore = inject(LocationStore)) => {
    const load = () => {
      const parameters: LocationChangesParameters = {
        pageSize: 25,
        pageIndex: 0,
      };

      apiService.locationChanges(locationStore.locationKey(), parameters).subscribe((response) => {
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
