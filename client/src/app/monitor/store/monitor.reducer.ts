import { routerNavigationAction } from '@ngrx/router-store';
import { createReducer } from '@ngrx/store';
import { on } from '@ngrx/store';
import { actionMonitorGroupChangesPageInit } from './monitor.actions';
import { actionMonitorChangesPageInit } from './monitor.actions';
import { actionMonitorRouteChangesPageInit } from './monitor.actions';
import { actionMonitorRouteChangesPageIndex } from './monitor.actions';
import { actionMonitorGroupChangesPageIndex } from './monitor.actions';
import { actionMonitorChangesPageIndex } from './monitor.actions';
import { actionMonitorChangesPageLoaded } from './monitor.actions';
import { actionMonitorGroupChangesPageLoaded } from './monitor.actions';
import { actionMonitorGroupPageLoaded } from './monitor.actions';
import { actionMonitorNavigateGroup } from './monitor.actions';
import { actionMonitorGroupUpdateLoaded } from './monitor.actions';
import { actionMonitorGroupDeleteLoaded } from './monitor.actions';
import { actionMonitorGroupsPageLoaded } from './monitor.actions';
import { actionMonitorAdmin } from './monitor.actions';
import { actionMonitorRouteChangePageLoaded } from './monitor.actions';
import { actionMonitorRouteMapReferenceVisible } from './monitor.actions';
import { actionMonitorRouteMapOkVisible } from './monitor.actions';
import { actionMonitorRouteMapOsmRelationVisible } from './monitor.actions';
import { actionMonitorRouteMapNokVisible } from './monitor.actions';
import { actionMonitorRouteMapMode } from './monitor.actions';
import { actionMonitorRouteChangesPageLoaded } from './monitor.actions';
import { actionMonitorRouteMapPageLoaded } from './monitor.actions';
import { actionMonitorRouteDetailsPageLoaded } from './monitor.actions';
import { initialState } from './monitor.state';

export const monitorReducer = createReducer(
  initialState,
  on(actionMonitorAdmin, (state, { admin }) => ({
    ...state,
    admin,
  })),
  on(routerNavigationAction, (state, action) => ({
    ...state,
    mapMode: null,
    routeGroups: null,
    changesPage: null,
    groupsPage: null,
    groupPage: null,
    groupChangesPage: null,
    adminGroupPage: null,
    routeDetailsPage: null,
    routeMapPage: null,
    routeChangesPage: null,
    routeChangePage: null,
  })),
  on(actionMonitorChangesPageInit, (state) => ({
    ...state,
    changesPageIndex: 0,
  })),
  on(actionMonitorChangesPageLoaded, (state, { response }) => ({
    ...state,
    changesPage: response,
  })),
  on(actionMonitorChangesPageIndex, (state, action) => ({
    ...state,
    changesPageIndex: action.pageIndex,
  })),
  on(actionMonitorGroupsPageLoaded, (state, { response }) => ({
    ...state,
    adminRole: response?.result?.adminRole === true,
    groupsPage: response,
  })),
  on(actionMonitorGroupPageLoaded, (state, { response }) => ({
    ...state,
    adminRole: response?.result?.adminRole === true,
    groupName: response?.result?.groupName ?? state.groupName,
    groupDescription:
      response?.result?.groupDescription ?? state.groupDescription,
    groupPage: response,
  })),
  on(actionMonitorGroupChangesPageInit, (state) => ({
    ...state,
    groupChangesPageIndex: 0,
  })),
  on(actionMonitorGroupChangesPageLoaded, (state, { response }) => ({
    ...state,
    groupName: response?.result?.groupName ?? state.groupName,
    groupDescription:
      response?.result?.groupDescription ?? state.groupDescription,
    groupChangesPage: response,
  })),
  on(actionMonitorGroupChangesPageIndex, (state, action) => ({
    ...state,
    groupChangesPageIndex: action.pageIndex,
  })),
  on(actionMonitorNavigateGroup, (state, { groupName, groupDescription }) => ({
    ...state,
    groupName,
    groupDescription,
  })),
  on(actionMonitorGroupDeleteLoaded, (state, { response }) => ({
    ...state,
    adminRole: response?.result?.adminRole === true,
    adminGroupPage: response,
  })),
  on(actionMonitorGroupUpdateLoaded, (state, { response }) => ({
    ...state,
    adminRole: response?.result?.adminRole === true,
    adminGroupPage: response,
  })),
  on(actionMonitorRouteDetailsPageLoaded, (state, { response }) => {
    const monitorRouteId =
      response.result?.monitorRouteId ?? state.monitorRouteId;
    const routeId = response.result?.routeId ?? state.routeId;
    const routeName = response.result?.routeName ?? state.routeName;
    const groupName = response.result?.groupName ?? state.groupName;
    const groupDescription =
      response.result?.groupDescription ?? state.groupDescription;
    return {
      ...state,
      monitorRouteId,
      routeId,
      routeName,
      groupName,
      groupDescription,
      routeDetailsPage: response,
    };
  }),
  on(actionMonitorRouteMapPageLoaded, (state, { response }) => {
    const monitorRouteId =
      response.result?.monitorRouteId ?? state.monitorRouteId;
    const routeId = response.result?.routeId ?? state.routeId;
    const routeName = response.result?.routeName ?? state.routeName;
    const groupName = response.result?.groupName ?? state.groupName;
    const groupDescription =
      response.result?.groupDescription ?? state.groupDescription;
    const mapGpxOkVisible = !!response.result?.okGeometry;
    const mapGpxNokVisible = (response.result?.nokSegments?.length ?? 0) > 0;
    const mapOsmRelationVisible =
      (response.result?.osmSegments?.length ?? 0) > 0;

    const mapGpxVisible = !(
      mapGpxOkVisible ||
      mapGpxNokVisible ||
      mapOsmRelationVisible
    );

    return {
      ...state,
      monitorRouteId,
      routeId,
      routeName,
      groupName,
      groupDescription,
      mapGpxVisible,
      mapGpxOkVisible,
      mapGpxNokVisible,
      mapOsmRelationVisible,
      mapMode: 'comparison',
      routeMapPage: response,
    };
  }),
  on(actionMonitorRouteChangesPageInit, (state) => ({
    ...state,
    routeChangesPageIndex: 0,
  })),
  on(actionMonitorRouteChangesPageLoaded, (state, { response }) => {
    const routeId = response.result?.routeId ?? state.routeId;
    const routeName = response.result?.routeName ?? state.routeName;
    const groupName = response.result?.groupName ?? state.groupName;
    const groupDescription =
      response.result?.groupDescription ?? state.groupDescription;
    return {
      ...state,
      routeId,
      routeName,
      groupName,
      groupDescription,
      routeChangesPage: response,
    };
  }),
  on(actionMonitorRouteChangesPageIndex, (state, action) => ({
    ...state,
    routeChangesPageIndex: action.pageIndex,
  })),
  on(actionMonitorRouteChangePageLoaded, (state, { response }) => {
    const routeId = response.result?.key.elementId ?? state.routeId;
    const routeName = 'ROUTE-NAME'; // response.result?.name ?? state.routeName;
    return {
      ...state,
      routeId,
      routeName,
      routeChangePage: response,
    };
  }),
  on(actionMonitorRouteMapMode, (state, { mode }) => {
    const mapGpxVisible = false;
    let mapGpxOkVisible = false;
    let mapGpxNokVisible = false;
    let mapOsmRelationVisible = false;
    if (mode === 'comparison') {
      mapGpxOkVisible = !!state.routeMapPage?.result?.reference.geometry;
      mapGpxNokVisible =
        (state.routeMapPage.result?.nokSegments?.length ?? 0) > 0;
      mapOsmRelationVisible =
        (state.routeMapPage.result?.osmSegments?.length ?? 0) > 0;
    } else if (mode === 'osm-segments') {
      mapOsmRelationVisible = true;
    }

    return {
      ...state,
      mapGpxVisible,
      mapGpxOkVisible,
      mapGpxNokVisible,
      mapOsmRelationVisible,
      mapMode: mode,
    };
  }),
  on(actionMonitorRouteMapReferenceVisible, (state, { visible }) => ({
    ...state,
    mapGpxVisible: visible,
  })),
  on(actionMonitorRouteMapOkVisible, (state, { visible }) => ({
    ...state,
    mapGpxOkVisible: visible,
  })),
  on(actionMonitorRouteMapNokVisible, (state, { visible }) => ({
    ...state,
    mapGpxNokVisible: visible,
  })),
  on(actionMonitorRouteMapOsmRelationVisible, (state, { visible }) => ({
    ...state,
    mapOsmRelationVisible: visible,
  }))
);
