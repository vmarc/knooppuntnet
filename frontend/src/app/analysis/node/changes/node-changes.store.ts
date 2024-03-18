import { Signal } from '@angular/core';
import { inject } from '@angular/core';
import { computed } from '@angular/core';
import { ChangesParameters } from '@api/common/changes/filter';
import { NodeChangesPage } from '@api/common/node';
import { ApiResponse } from '@api/custom';
import { Util } from '@app/components/shared';
import { ChangeOption } from '@app/kpn/common';
import { ApiService } from '@app/services';
import { PageParams } from '@app/shared/base';
import { withHooks } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withComputed } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { PreferencesStore } from '../../../shared/core/preferences/preferences.store';
import { RouterService } from '../../../shared/services/router.service';
import { NodeStore } from '../node.store';

export type NodeChangesState = {
  response: ApiResponse<NodeChangesPage>;
  changesParameters: ChangesParameters;
};

export const initialState: NodeChangesState = {
  response: null,
  changesParameters: null,
};

export const NodeChangesStore = signalStore(
  withState(initialState),
  withComputed((state) => ({
    pageIndex: computed(() => state.changesParameters()?.pageIndex),
    filterOptions: computed(() => state.response()?.result?.filterOptions),
  })),
  withMethods((store) => {
    const apiService = inject(ApiService);
    const routerService = inject(RouterService);
    const preferencesStore = inject(PreferencesStore);
    const nodeStore = inject(NodeStore);
    const load = (): void => {
      apiService
        .nodeChanges(nodeStore.nodeId(), store.changesParameters())
        .subscribe((response) => {
          if (response.result) {
            nodeStore.updateNode(response.result.nodeName, response.result.changeCount);
          }
          patchState(store, {
            response,
          });
        });
    };

    return {
      init: (): void => {
        nodeStore.initPage();
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
      impact: (): Signal<boolean> => {
        return preferencesStore.impact;
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
        preferencesStore.updateImpact(impact);
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
