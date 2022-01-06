import { routerNavigationAction } from '@ngrx/router-store';
import { createReducer } from '@ngrx/store';
import { on } from '@ngrx/store';
import { actionNodeMapPageLoad } from './node.actions';
import { actionNodeDetailsPageLoad } from './node.actions';
import { actionNodeChangesPageLoad } from './node.actions';
import { actionNodeChangesPageImpact } from './node.actions';
import { actionNodeChangesPageSize } from './node.actions';
import { actionNodeChangesPageIndex } from './node.actions';
import { actionNodeChangesFilterOption } from './node.actions';
import { actionNodeLink } from './node.actions';
import { actionNodeDetailsPageLoaded } from './node.actions';
import { actionNodeMapPageLoaded } from './node.actions';
import { actionNodeChangesPageLoaded } from './node.actions';
import { initialState } from './node.state';

export const nodeReducer = createReducer(
  initialState,
  on(routerNavigationAction, (state, {}) => ({
    ...state,
    detailsPage: null,
    mapPage: null,
    changesPage: null,
  })),
  on(actionNodeDetailsPageLoad, actionNodeMapPageLoad, (state, { nodeId }) => ({
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
  on(actionNodeChangesPageLoad, (state, { nodeId, changesParameters }) => ({
    ...state,
    nodeId,
    changesParameters,
  })),
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
  on(actionNodeChangesPageImpact, (state, action) => ({
    ...state,
    changesParameters: {
      ...state.changesParameters,
      impact: action.impact,
      pageIndex: 0,
    },
  })),
  on(actionNodeChangesPageSize, (state, action) => ({
    ...state,
    changesParameters: {
      ...state.changesParameters,
      pageSize: action.pageSize,
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
