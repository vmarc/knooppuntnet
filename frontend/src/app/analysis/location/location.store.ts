import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { LocationSummary } from '@api/common/location';
import { LocationKey } from '@api/custom';
import { withComputed } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { RouterService } from '../../shared/services/router.service';

export interface LocationState {
  locationKey: LocationKey | null;
  locationSummary: LocationSummary | null;
}

const initialState: LocationState = {
  locationKey: null,
  locationSummary: null,
};

export const LocationStore = signalStore(
  { providedIn: 'root' }, // provided in root: all location pages share this state
  withState(initialState),
  withMethods((store) => {
    function shouldUpdate(oldKey: LocationKey, newKey: LocationKey): boolean {
      return (
        !oldKey ||
        oldKey.networkType !== newKey.networkType ||
        oldKey.country !== newKey.country ||
        oldKey.name !== newKey.name
      );
    }

    return {
      initLocationPage: () => {
        const routerService = inject(RouterService);
        const locationKey: LocationKey = {
          networkType: routerService.paramNetworkType(),
          country: routerService.paramCountry(),
          name: routerService.param('location'),
        };
        if (shouldUpdate(store.locationKey(), locationKey)) {
          patchState(store, {
            locationKey,
            locationSummary: null,
          });
        }
      },
      updateSummary: (locationSummary: LocationSummary) => {
        patchState(store, {
          locationSummary,
        });
      },
    };
  }),
  withComputed((state) => ({
    key: computed(() => state.locationKey()),
    summary: computed(() => state.locationSummary()),
  }))
);
