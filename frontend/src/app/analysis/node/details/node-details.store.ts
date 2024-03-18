import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { NodeDetailsPage } from '@api/common/node';
import { ApiResponse } from '@api/custom';
import { NetworkTypes } from '@app/kpn/common';
import { ApiService } from '@app/services';
import { withComputed } from '@ngrx/signals';
import { withHooks } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { NodeStore } from '../node.store';

export type NodeDetailsState = {
  response: ApiResponse<NodeDetailsPage>;
};

export const initialState: NodeDetailsState = {
  response: null,
};

export const NodeDetailsStore = signalStore(
  withState(initialState),
  withComputed((state) => ({
    networkTypes: computed(() => {
      const resp = state.response();
      if (resp) {
        const networkTypes = resp.result.nodeInfo.names.map((nodeName) => nodeName.networkType);
        return NetworkTypes.all.filter((networkType) => networkTypes.includes(networkType));
      }
      return [];
    }),
  })),
  withMethods((store) => {
    const apiService = inject(ApiService);
    const nodeStore = inject(NodeStore);
    const load = (): void => {
      apiService.nodeDetails(nodeStore.nodeId()).subscribe((response) => {
        if (response.result) {
          nodeStore.updateNode(response.result.nodeInfo.name, response.result.changeCount);
        }
        patchState(store, {
          response,
        });
      });
    };

    return {
      init: (): void => {
        nodeStore.initPage();
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
