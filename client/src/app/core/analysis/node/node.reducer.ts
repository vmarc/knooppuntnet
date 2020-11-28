import {routerNavigationAction} from '@ngrx/router-store';
import {createReducer} from '@ngrx/store';
import {on} from '@ngrx/store';
import {actionNodeLink} from './node.actions';
import {actionNodeDetailsLoaded} from './node.actions';
import {actionNodeMapLoaded} from './node.actions';
import {actionNodeChangesLoaded} from './node.actions';
import {initialState} from './node.state';

export const nodeReducer = createReducer(
  initialState,
  on(
    routerNavigationAction,
    (state, action) => ({
      ...state,
      details: null,
      map: null,
      changes: null
    })
  ),
  on(
    actionNodeLink,
    (state, {nodeId, nodeName}) => ({
      ...state,
      nodeId: nodeId,
      nodeName: nodeName,
      changeCount: 0
    })
  ),
  on(
    actionNodeDetailsLoaded,
    (state, {response}) => {
      const nodeId = response.result?.nodeInfo.id.toString() ?? state.nodeId;
      const nodeName = response.result?.nodeInfo.name ?? state.nodeName;
      const changeCount = response.result?.changeCount ?? state.changeCount;
      return {
        ...state,
        nodeId: nodeId,
        nodeName: nodeName,
        changeCount: changeCount,
        details: response
      };
    }
  ),
  on(
    actionNodeMapLoaded,
    (state, {response}) => {
      const nodeId = response.result?.nodeMapInfo.id.toString() ?? state.nodeId;
      const nodeName = response.result?.nodeMapInfo.name ?? state.nodeName;
      const changeCount = response.result?.changeCount ?? state.changeCount;
      return {
        ...state,
        nodeId: nodeId,
        nodeName: nodeName,
        changeCount: changeCount,
        map: response
      };
    }
  ),
  on(
    actionNodeChangesLoaded,
    (state, {response}) => {
      const nodeId = response.result?.nodeInfo.id.toString() ?? state.nodeId;
      const nodeName = response.result?.nodeInfo.name ?? state.nodeName;
      const changeCount = response.result?.changeCount ?? state.changeCount;
      return {
        ...state,
        nodeId: nodeId,
        nodeName: nodeName,
        changeCount: changeCount,
        changes: response
      };
    }
  )
);
