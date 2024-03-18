import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangesParameters } from '@api/common/changes/filter';
import { SubsetChangesPage } from '@api/common/subset';
import { ApiResponse } from '@api/custom';
import { Util } from '@app/components/shared';
import { ApiService } from '@app/services';
import { PageParams } from '@app/shared/base';
import { withComputed } from '@ngrx/signals';
import { withHooks } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { PreferencesStore } from '../../../shared/core/preferences/preferences.store';
import { RouterService } from '../../../shared/services/router.service';
import { SubsetStore } from '../subset.store';

export type SubsetChangesState = {
  changesParameters: ChangesParameters | null;
  response: ApiResponse<SubsetChangesPage> | null;
};

const initialState: SubsetChangesState = {
  changesParameters: null,
  response: null,
};

export const SubsetChangesStore = signalStore(
  withState(initialState),
  withComputed((state) => ({
    impact: computed(() => state.changesParameters().impact),
    pageSize: computed(() => state.changesParameters().pageSize),
    pageIndex: computed(() => state.changesParameters().pageIndex),
  })),
  withMethods((store) => {
    const apiService = inject(ApiService);
    const subsetStore = inject(SubsetStore);
    const preferencesStore = inject(PreferencesStore);
    const routerService = inject(RouterService);

    const load = (): void => {
      apiService
        .subsetChanges(subsetStore.subset(), store.changesParameters())
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
        const params = routerService.params();
        const queryParams = routerService.queryParams();
        const uniqueQueryParams = Util.uniqueParams(queryParams);
        const pageParams = new PageParams(params, uniqueQueryParams);

        const preferencesImpact = preferencesStore.impact();
        const preferencesPageSize = preferencesStore.pageSize();

        const changesParameters = pageParams.changesParameters(
          preferencesImpact,
          preferencesPageSize
        );

        patchState(store, {
          changesParameters,
        });
        load();
      },
      updatePageSize: (pageSize: number): void => {
        preferencesStore.updatePageSize(pageSize);
        patchState(store, {
          changesParameters: {
            ...store.changesParameters(),
            pageIndex: 0,
            pageSize,
          },
        });
        load();
      },
      updateImpact: (impact: boolean): void => {
        patchState(store, {
          changesParameters: {
            ...store.changesParameters(),
            pageIndex: 0,
            impact,
          },
        });
        load();
      },
      updatePageIndex: (pageIndex: number): void => {
        patchState(store, {
          changesParameters: {
            ...store.changesParameters(),
            pageIndex,
          },
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
