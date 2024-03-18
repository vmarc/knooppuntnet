import { inject } from '@angular/core';
import { SubsetFactsPage } from '@api/common/subset';
import { ApiResponse } from '@api/custom';
import { ApiService } from '@app/services';
import { withHooks } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { SubsetStore } from '../subset.store';

export type SubsetFactsState = {
  response: ApiResponse<SubsetFactsPage>;
};

const initialState: SubsetFactsState = {
  response: null,
};

export const SubsetFactsStore = signalStore(
  withState(initialState),
  withMethods((store, subsetStore = inject(SubsetStore)) => {
    const apiService = inject(ApiService);
    const load = (): void => {
      apiService.subsetFacts(subsetStore.subset()).subscribe((response) => {
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
    };
  }),
  withHooks({
    onInit({ init }) {
      init();
    },
  })
);
