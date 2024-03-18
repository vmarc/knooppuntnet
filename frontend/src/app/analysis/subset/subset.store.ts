import { inject } from '@angular/core';
import { SubsetInfo } from '@api/common/subset';
import { Subset } from '@api/custom';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { RouterService } from '../../shared/services/router.service';

export type SubsetState = {
  subset: Subset;
  subsetInfo: SubsetInfo;
};

const initialState: SubsetState = {
  subset: null,
  subsetInfo: null,
};

export const SubsetStore = signalStore(
  { providedIn: 'root' }, // provided in root: all subset pages share this state
  withState(initialState),
  withMethods((store) => {
    return {
      initPage: (): void => {
        const routerService = inject(RouterService);
        const subset = routerService.paramSubset();
        const oldSubset = store.subset();
        if (
          !oldSubset ||
          oldSubset.country !== subset.country ||
          oldSubset.networkType !== subset.networkType
        ) {
          patchState(store, {
            subset,
            subsetInfo: null,
          });
        }
      },
      updateSubsetInfo: (subsetInfo: SubsetInfo): void => {
        patchState(store, {
          subsetInfo,
        });
      },
    };
  })
);
