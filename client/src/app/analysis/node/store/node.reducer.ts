import { routerNavigationAction } from '@ngrx/router-store';
import { createReducer } from '@ngrx/store';
import { on } from '@ngrx/store';
import { actionNodeLink } from './node.actions';
import { actionNodeDetailsPageLoaded } from './node.actions';
import { actionNodeMapPageLoaded } from './node.actions';
import { actionNodeChangesPageLoaded } from './node.actions';
import { initialState } from './node.state';

export const nodeReducer = createReducer(
  initialState,
  on(routerNavigationAction, (state, action) => ({
    ...state,
    detailsPage: null,
    mapPage: null,
    changesPage: null,
  })),
  on(actionNodeLink, (state, { nodeId, nodeName }) => ({
    ...state,
    nodeId,
    nodeName,
    changeCount: 0,
  })),
  on(actionNodeDetailsPageLoaded, (state, { response }) => {
    const nodeId = response.result?.nodeInfo.id.toString() ?? state.nodeId;
    const nodeName = response.result?.nodeInfo.name ?? state.nodeName;
    const changeCount = response.result?.changeCount ?? state.changeCount;
    return {
      ...state,
      nodeId,
      nodeName,
      changeCount,
      detailsPage: response,
    };
  }),
  on(actionNodeMapPageLoaded, (state, { response }) => {
    const nodeId = response.result?.nodeMapInfo.id.toString() ?? state.nodeId;
    const nodeName = response.result?.nodeMapInfo.name ?? state.nodeName;
    const changeCount = response.result?.changeCount ?? state.changeCount;
    return {
      ...state,
      nodeId,
      nodeName,
      changeCount,
      mapPage: response,
    };
  }),
  on(actionNodeChangesPageLoaded, (state, { response }) => {
    const nodeId = response.result?.nodeId.toString() ?? state.nodeId;
    const nodeName = response.result?.nodeName ?? state.nodeName;
    const changeCount = response.result?.changeCount ?? state.changeCount;
    return {
      ...state,
      nodeId,
      nodeName,
      changeCount,
      changesPage: response,
    };
  })
);
