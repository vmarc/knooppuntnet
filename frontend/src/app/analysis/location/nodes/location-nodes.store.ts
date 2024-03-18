import { Signal } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { LocationNodesParameters } from '@api/common/location';
import { LocationNodesPage } from '@api/common/location';
import { NetworkType } from '@api/custom';
import { ApiResponse } from '@api/custom';
import { LocationNodesType } from '@api/custom';
import { ApiService } from '@app/services';
import { withHooks } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { PreferencesStore } from '../../../shared/core/preferences/preferences.store';
import { LocationStore } from '../location.store';

type LocationNodesState = {
  pageType: LocationNodesType | null;
  pageIndex: number | null;
  response: ApiResponse<LocationNodesPage> | null;
};

const initialState: LocationNodesState = {
  pageType: null,
  pageIndex: null,
  response: null,
};

export const LocationNodesStore = signalStore(
  withState(initialState),
  withMethods((store) => {
    const apiService = inject(ApiService);
    const locationStore = inject(LocationStore);
    const preferencesStore = inject(PreferencesStore);

    const load = (): void => {
      const parameters: LocationNodesParameters = {
        locationNodesType: store.pageType(),
        pageSize: preferencesStore.pageSize(),
        pageIndex: store.pageIndex(),
      };
      apiService.locationNodes(locationStore.locationKey(), parameters).subscribe((response) => {
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
          pageType: LocationNodesType.all,
          pageIndex: 0,
        });
        load();
      },
      networkType: (): Signal<NetworkType> => {
        return computed(() => locationStore.key().networkType);
      },
      pageSize: (): Signal<number> => {
        return preferencesStore.pageSize;
      },
      updatePageSize: (pageSize: number): void => {
        preferencesStore.updatePageSize(pageSize);
        load();
      },
      updatePageIndex: (pageIndex: number): void => {
        patchState(store, {
          pageIndex,
        });
        load();
      },
      updatePageType: (pageType: LocationNodesType): void => {
        patchState(store, {
          pageType,
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
