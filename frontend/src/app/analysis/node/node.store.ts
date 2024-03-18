import { Location } from '@angular/common';
import { inject } from '@angular/core';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { RouterService } from '../../shared/services/router.service';

export type NodeState = {
  nodeId: string;
  nodeName: string;
  changeCount: number;
};

export const initialState: NodeState = {
  nodeId: '',
  nodeName: '',
  changeCount: 0,
};

export const NodeStore = signalStore(
  { providedIn: 'root' }, // provided in root: all node pages share this state
  withState(initialState),
  withMethods((store) => {
    return {
      initPage: (): void => {
        const location = inject(Location);
        const routerService = inject(RouterService);
        const nodeId = routerService.param('nodeId');
        const oldNodeId = store.nodeId();
        if (!oldNodeId || oldNodeId !== nodeId) {
          let nodeName: string = undefined;
          const state = location.getState();
          if (state) {
            nodeName = state['nodeName'];
            patchState(store, {
              nodeId,
              nodeName,
              changeCount: 0,
            });
          }
        }
      },
      updateNode: (nodeName: string, changeCount: number): void => {
        patchState(store, {
          nodeName,
          changeCount,
        });
      },
    };
  })
);
