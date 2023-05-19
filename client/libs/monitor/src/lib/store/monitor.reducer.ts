import { createReducer } from '@ngrx/store';
import { on } from '@ngrx/store';
import { actionMonitorRouteSaveDestroy } from './monitor.actions';
import { actionMonitorChangesPageDestroy } from './monitor.actions';
import { actionMonitorRouteChangePageDestroy } from './monitor.actions';
import { actionMonitorRouteChangesPageDestroy } from './monitor.actions';
import { actionMonitorGroupChangesPageDestroy } from './monitor.actions';
import { actionMonitorRouteSaved } from './monitor.actions';
import { actionMonitorRouteSaveInit } from './monitor.actions';
import { actionMonitorRouteAdminRelationIdChanged } from './monitor.actions';
import { actionMonitorGroupChangesPageInit } from './monitor.actions';
import { actionMonitorChangesPageInit } from './monitor.actions';
import { actionMonitorRouteChangesPageInit } from './monitor.actions';
import { actionMonitorRouteChangesPageIndex } from './monitor.actions';
import { actionMonitorGroupChangesPageIndex } from './monitor.actions';
import { actionMonitorChangesPageIndex } from './monitor.actions';
import { actionMonitorChangesPageLoaded } from './monitor.actions';
import { actionMonitorGroupChangesPageLoaded } from './monitor.actions';
import { actionMonitorRouteChangePageLoaded } from './monitor.actions';
import { actionMonitorRouteChangesPageLoaded } from './monitor.actions';
import { MonitorState } from './monitor.state';
import { MonitorRouteSaveState } from './monitor.state';
import { initialState } from './monitor.state';

export const monitorReducer = createReducer<MonitorState>(
  initialState,
  on(
    actionMonitorChangesPageInit,
    (state): MonitorState => ({
      ...state,
      changesPageIndex: 0,
    })
  ),
  on(
    actionMonitorChangesPageDestroy,
    (state): MonitorState => ({
      ...state,
      changesPage: undefined,
      changesPageIndex: undefined,
    })
  ),
  on(
    actionMonitorChangesPageLoaded,
    (state, response): MonitorState => ({
      ...state,
      changesPage: response,
    })
  ),
  on(
    actionMonitorChangesPageIndex,
    (state, action): MonitorState => ({
      ...state,
      changesPageIndex: action.pageIndex,
    })
  ),
  on(
    actionMonitorGroupChangesPageInit,
    (state): MonitorState => ({
      ...state,
      groupChangesPageIndex: 0,
    })
  ),
  on(
    actionMonitorGroupChangesPageLoaded,
    (state, response): MonitorState => ({
      ...state,
      groupName: response?.result?.groupName ?? state.groupName,
      groupDescription:
        response?.result?.groupDescription ?? state.groupDescription,
      groupChangesPage: response,
    })
  ),
  on(
    actionMonitorGroupChangesPageDestroy,
    (state): MonitorState => ({
      ...state,
      groupName: undefined,
      groupDescription: undefined,
      groupChangesPage: undefined,
    })
  ),
  on(
    actionMonitorGroupChangesPageIndex,
    (state, action): MonitorState => ({
      ...state,
      groupChangesPageIndex: action.pageIndex,
    })
  ),
  on(actionMonitorRouteAdminRelationIdChanged, (state): MonitorState => {
    return {
      ...state,
      routeInfoPage: null,
    };
  }),
  on(actionMonitorRouteSaveInit, (state, parameters): MonitorState => {
    let routeSaveState: MonitorRouteSaveState;
    if (parameters.mode === 'add') {
      const gpx = parameters.properties.referenceType === 'gpx';
      routeSaveState = {
        ...new MonitorRouteSaveState(),
        saveRouteEnabled: true,
        saveRouteStatus: 'busy',
        uploadGpxEnabled: gpx,
        analyzeEnabled: gpx,
      };
    } else {
      const gpx =
        parameters.properties.referenceType === 'gpx' &&
        parameters.properties.referenceFileChanged;
      routeSaveState = {
        ...new MonitorRouteSaveState(),
        saveRouteEnabled: true,
        saveRouteStatus: 'busy',
        uploadGpxEnabled: gpx,
        analyzeEnabled: gpx,
      };
    }
    return {
      ...state,
      routeSaveState,
    };
  }),
  on(actionMonitorRouteSaveDestroy, (state): MonitorState => {
    return {
      ...state,
      routeSaveState: undefined,
    };
  }),
  on(actionMonitorRouteSaved, (state, response): MonitorState => {
    return {
      ...state,
      routeSaveState: {
        ...state.routeSaveState,
        saveRouteStatus: 'done',
        errors: response.result?.errors,
        done: true,
      },
    };
  }),
  on(
    actionMonitorRouteChangesPageInit,
    (state): MonitorState => ({
      ...state,
      routeChangesPageIndex: 0,
    })
  ),
  on(actionMonitorRouteChangesPageLoaded, (state, response): MonitorState => {
    const result = response.result;
    const routeName = result?.routeName ?? state.routeName;
    const groupName = result?.groupName ?? state.groupName;
    const groupDescription = result?.groupDescription ?? state.groupDescription;
    return {
      ...state,
      routeName,
      groupName,
      groupDescription,
      routeChangesPage: response,
    };
  }),
  on(actionMonitorRouteChangesPageDestroy, (state): MonitorState => {
    return {
      ...state,
      routeName: undefined,
      groupName: undefined,
      groupDescription: undefined,
      routeChangesPage: undefined,
    };
  }),
  on(
    actionMonitorRouteChangesPageIndex,
    (state, action): MonitorState => ({
      ...state,
      routeChangesPageIndex: action.pageIndex,
    })
  ),
  on(actionMonitorRouteChangePageLoaded, (state, response): MonitorState => {
    const routeId = 'TODO MON'; // response.result?.key.elementId ?? state.routeId;
    const routeName = 'ROUTE-NAME'; // response.result?.name ?? state.routeName;
    return {
      ...state,
      routeName,
      routeChangePage: response,
    };
  }),
  on(actionMonitorRouteChangePageDestroy, (state): MonitorState => {
    return {
      ...state,
      routeName: undefined,
      routeChangePage: undefined,
    };
  })
);
