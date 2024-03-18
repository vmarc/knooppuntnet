import { inject } from '@angular/core';
import { SubsetFactDetailsPage } from '@api/common/subset';
import { ApiResponse } from '@api/custom';
import { SubsetFact } from '@app/kpn/common';
import { ApiService } from '@app/services';
import { PageParams } from '@app/shared/base';
import { withHooks } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { RouterService } from '../../../shared/services/router.service';
import { SubsetStore } from '../subset.store';

export type SubsetFactDetailsState = {
  subsetFact: SubsetFact;
  response: ApiResponse<SubsetFactDetailsPage>;
};

const initialState: SubsetFactDetailsState = {
  subsetFact: null,
  response: null,
};

export const SubsetFactDetailsStore = signalStore(
  withState(initialState),
  withMethods((store, subsetStore = inject(SubsetStore)) => {
    const apiService = inject(ApiService);
    const routerService = inject(RouterService);
    const load = (): void => {
      apiService
        .subsetFactDetails(subsetStore.subset(), store.subsetFact().factName)
        .subscribe((response) => {
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
        const subsetFact = new PageParams(routerService.params()).subsetFact();
        patchState(store, {
          subsetFact,
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
