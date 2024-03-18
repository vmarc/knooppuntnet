import { inject } from '@angular/core';
import { SubsetMapPage } from '@api/common/subset';
import { ApiResponse } from '@api/custom';
import { ApiService } from '@app/services';
import { withHooks } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { SubsetStore } from '../subset.store';
import { SubsetMapService } from './subset-map.service';

export type SubsetMapState = {
  response: ApiResponse<SubsetMapPage>;
};

const initialState: SubsetMapState = {
  response: null,
};

export const SubsetMapStore = signalStore(
  withState(initialState),
  withMethods((store) => {
    const apiService = inject(ApiService);
    const subsetStore = inject(SubsetStore);
    const subsetMapService = inject(SubsetMapService);

    const load = (): void => {
      apiService.subsetMap(subsetStore.subset()).subscribe((response) => {
        if (response.result) {
          subsetStore.updateSubsetInfo(response.result.subsetInfo);
        }
        patchState(store, {
          response,
        });
      });
    };

    return {
      init: (): void => {
        subsetStore.initPage();
        load();
      },
      afterViewInit: (): void => {
        const response = store.response();
        subsetMapService.init(response.result.networks, response.result.bounds);
      },
      destroy: (): void => {
        subsetMapService.destroy();
      },
    };
  }),
  withHooks({
    onInit({ init }) {
      init();
    },
    onDestroy({ destroy }) {
      destroy();
    },
  })
);
