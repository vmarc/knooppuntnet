import { computed } from '@angular/core';
import { Signal } from '@angular/core';
import { inject } from '@angular/core';
import { LocationRoutesParameters } from '@api/common/location';
import { LocationRoutesPage } from '@api/common/location';
import { NetworkType } from '@api/custom';
import { LocationRoutesType } from '@api/custom';
import { ApiResponse } from '@api/custom';
import { ApiService } from '@app/services';
import { withHooks } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { PreferencesStore } from '../../../shared/core/preferences/preferences.store';
import { LocationStore } from '../location.store';

type LocationRoutesState = {
  pageType: LocationRoutesType | null;
  pageIndex: number | null;
  response: ApiResponse<LocationRoutesPage> | null;
};

const initialState: LocationRoutesState = {
  pageType: null,
  pageIndex: null,
  response: null,
};

export const LocationRoutesStore = signalStore(
  withState(initialState),
  withMethods((store) => {
    const apiService = inject(ApiService);
    const locationStore = inject(LocationStore);
    const preferencesStore = inject(PreferencesStore);

    const load = (): void => {
      const parameters: LocationRoutesParameters = {
        locationRoutesType: store.pageType(),
        pageSize: preferencesStore.pageSize(),
        pageIndex: store.pageIndex(),
      };
      apiService.locationRoutes(locationStore.locationKey(), parameters).subscribe((response) => {
        if (response.result) {
          locationStore.updateSummary(response.result.summary);
        }
        patchState(store, {
          response,
        });
      });
    };

    return {
      init: (): void => {
        locationStore.initLocationPage();
        patchState(store, {
          pageType: LocationRoutesType.all,
          pageIndex: 0,
        });
        load();
      },
      networkType: (): Signal<NetworkType> => {
        return computed(() => locationStore.key().networkType);
      },
      pageSize: (): Signal<number> => {
        return computed(() => preferencesStore.pageSize());
      },
      updatePageSize: (pageSize: number): void => {
        preferencesStore.updatePageSize(pageSize);
        patchState(store, {
          pageIndex: 0,
        });
        load();
      },
      updatePageIndex: (pageIndex: number): void => {
        patchState(store, {
          pageIndex,
        });
        load();
      },
      updatePageType: (pageType: LocationRoutesType): void => {
        patchState(store, {
          pageType,
          pageIndex: 0,
        });
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
