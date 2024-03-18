import { computed } from '@angular/core';
import { Signal } from '@angular/core';
import { inject } from '@angular/core';
import { ChangesParameters } from '@api/common/changes/filter';
import { NetworkChangesPage } from '@api/common/network';
import { ApiResponse } from '@api/custom';
import { Util } from '@app/components/shared';
import { ChangeOption } from '@app/kpn/common';
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
import { NetworkStore } from '../network.store';

export type NetworkChangesState = {
  response: ApiResponse<NetworkChangesPage>;
  changesParameters: ChangesParameters;
};

export const initialState: NetworkChangesState = {
  response: null,
  changesParameters: null,
};

export const NetworkChangesStore = signalStore(
  withState(initialState),
  withComputed((state) => ({
    impact: computed(() => state.changesParameters().impact),
    pageSize: computed(() => state.changesParameters().pageSize),
    pageIndex: computed(() => state.changesParameters().pageIndex),
    filterOptions: computed(() => state.response()?.result?.filterOptions),
  })),
  withMethods((store) => {
    const apiService = inject(ApiService);
    const networkStore = inject(NetworkStore);
    const preferencesStore = inject(PreferencesStore);
    const routerService = inject(RouterService);
    const load = (): void => {
      const params = routerService.params();
      const queryParams = routerService.queryParams();
      const uniqueQueryParams = Util.uniqueParams(queryParams);
      const pageParams = new PageParams(params, uniqueQueryParams);

      const preferencesImpact = preferencesStore.impact();
      const preferencesPageSize = preferencesStore.pageSize();

      const parameters = pageParams.changesParameters(preferencesImpact, preferencesPageSize);

      apiService.networkChanges(networkStore.networkId(), parameters).subscribe((response) => {
        if (response.result) {
          networkStore.updateSummary(response.result.network);
        }
        patchState(store, {
          response,
        });
      });
    };

    return {
      init: (): void => {
        networkStore.initPage();
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
      pageSize: (): Signal<number> => {
        return preferencesStore.pageSize;
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
      updateFilterOption: (option: ChangeOption): void => {
        patchState(store, {
          changesParameters: {
            ...store.changesParameters(),
            year: option.year,
            month: option.month,
            day: option.day,
            impact: option.impact,
            pageIndex: 0,
          },
        });
      },
    };
  }),
  withHooks({
    onInit({ init }) {
      init();
    },
  })
);
