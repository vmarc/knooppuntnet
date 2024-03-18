import { Signal } from '@angular/core';
import { inject } from '@angular/core';
import { SubsetOrphanNodesPage } from '@api/common/subset';
import { ApiResponse } from '@api/custom';
import { ApiService } from '@app/services';
import { withHooks } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { PreferencesStore } from '../../../shared/core/preferences/preferences.store';
import { SubsetStore } from '../subset.store';

export type SubsetOrphanNodesState = {
  response: ApiResponse<SubsetOrphanNodesPage>;
  pageIndex: number;
};

const initialState: SubsetOrphanNodesState = {
  response: null,
  pageIndex: 0,
};

export const SubsetOrphanNodesStore = signalStore(
  withState(initialState),
  withMethods((store) => {
    const apiService = inject(ApiService);
    const subsetStore = inject(SubsetStore);
    const preferencesStore = inject(PreferencesStore);
    const load = (): void => {
      apiService.subsetOrphanNodes(subsetStore.subset()).subscribe((response) => {
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
    };
  }),
  withHooks({
    onInit({ init }) {
      init();
    },
  })
);
