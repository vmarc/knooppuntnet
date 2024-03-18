import { inject } from '@angular/core';
import { NetworkSummary } from '@api/common/network';
import { NetworkType } from '@api/custom';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { RouterService } from '../../shared/services/router.service';
import { Location } from '@angular/common';

const defaultSummary: NetworkSummary = {
  name: '',
  networkType: null,
  networkScope: null,
  factCount: 0,
  nodeCount: 0,
  routeCount: 0,
  changeCount: 0,
};

export type NetworkState = {
  networkId: number | null;
  summary: NetworkSummary;
};

export const initialState: NetworkState = {
  networkId: null,
  summary: defaultSummary,
};

export const NetworkStore = signalStore(
  { providedIn: 'root' }, // provided in root: all network pages share this state
  withState(initialState),
  withMethods((store) => {
    return {
      initPage: (): void => {
        const location = inject(Location);
        const routerService = inject(RouterService);
        const networkIdParam = routerService.param('networkId');
        const networkId = +networkIdParam;
        const oldNetworkId = store.networkId();
        if (!oldNetworkId || oldNetworkId !== networkId) {
          const networkId = +networkIdParam;
          let summary = defaultSummary;
          const state = location.getState();
          if (state) {
            const networkType = state['networkType'];
            const name = state['networkName'];
            summary = {
              ...defaultSummary,
              name,
              networkType,
            };
          }
          patchState(store, {
            networkId,
            summary,
          });
        }
      },
      updateSummary: (summary: NetworkSummary): void => {
        patchState(store, {
          summary,
        });
      },
    };
  })
);
