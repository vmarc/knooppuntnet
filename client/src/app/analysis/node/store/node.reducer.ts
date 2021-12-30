import { routerNavigationAction } from '@ngrx/router-store';
import { createReducer } from '@ngrx/store';
import { on } from '@ngrx/store';
import { actionPreferencesItemsPerPage } from '../../../core/preferences/preferences.actions';
import { actionPreferencesImpact } from '../../../core/preferences/preferences.actions';
import { actionNodeChangesPageIndex } from './node.actions';
import { actionNodeChangesFilterOption } from './node.actions';
import { actionNodeId } from './node.actions';
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
  on(actionNodeId, (state, { nodeId }) => ({
    ...state,
    nodeId,
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
  }),
  on(actionPreferencesImpact, (state, action) => ({
    ...state,
    changesParameters: {
      ...state.changesParameters,
      impact: action.impact,
      pageIndex: 0,
    },
  })),
  on(actionPreferencesItemsPerPage, (state, action) => ({
    ...state,
    changesParameters: {
      ...state.changesParameters,
      itemsPerPage: action.itemsPerPage,
      pageIndex: 0,
    },
  })),
  on(actionNodeChangesPageIndex, (state, action) => ({
    ...state,
    changesParameters: {
      ...state.changesParameters,
      pageIndex: action.pageIndex,
    },
  })),
  on(actionNodeChangesFilterOption, (state, action) => ({
    ...state,
    changesParameters: {
      ...state.changesParameters,
      year: action.option.year,
      month: action.option.month,
      day: action.option.day,
      impact: action.option.impact,
      pageIndex: 0,
    },
  }))
);
