import { createReducer } from '@ngrx/store';
import { on } from '@ngrx/store';
import { actionMonitorRouteMapPageLoaded } from '../route/map/store/monitor-route-map.actions';
import { actionMonitorRouteGpxPageDestroy } from './monitor.actions';
import { actionMonitorRouteGpxPageLoaded } from './monitor.actions';
import { actionMonitorRouteGpxPageLoad } from './monitor.actions';
import { actionMonitorRouteAddPageLoad } from './monitor.actions';
import { actionMonitorRouteDetailsPageLoad } from './monitor.actions';
import { actionMonitorRouteSaveDestroy } from './monitor.actions';
import { actionMonitorChangesPageDestroy } from './monitor.actions';
import { actionMonitorRouteUpdatePageDestroy } from './monitor.actions';
import { actionMonitorRouteAddPageDestroy } from './monitor.actions';
import { actionMonitorRouteChangePageDestroy } from './monitor.actions';
import { actionMonitorRouteChangesPageDestroy } from './monitor.actions';
import { actionMonitorRouteDetailsPageDestroy } from './monitor.actions';
import { actionMonitorGroupChangesPageDestroy } from './monitor.actions';
import { actionMonitorGroupPageDestroy } from './monitor.actions';
import { actionMonitorGroupUpdateDestroy } from './monitor.actions';
import { actionMonitorGroupDeleteDestroy } from './monitor.actions';
import { actionMonitorGroupsPageDestroy } from './monitor.actions';
import { actionMonitorRouteSaved } from './monitor.actions';
import { actionMonitorRouteUploadInit } from './monitor.actions';
import { actionMonitorRouteUploaded } from './monitor.actions';
import { actionMonitorRouteAnalyzed } from './monitor.actions';
import { actionMonitorRouteSaveInit } from './monitor.actions';
import { actionMonitorRouteAdminRelationIdChanged } from './monitor.actions';
import { actionMonitorRouteUpdatePageLoaded } from './monitor.actions';
import { actionMonitorRouteAddPageLoaded } from './monitor.actions';
import { actionMonitorRouteInfoLoaded } from './monitor.actions';
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
import { actionMonitorRouteChangesPageLoaded } from './monitor.actions';
import { actionMonitorRouteDetailsPageLoaded } from './monitor.actions';
import { MonitorState } from './monitor.state';
import { MonitorRouteSaveState } from './monitor.state';
import { initialState } from './monitor.state';

export const monitorReducer = createReducer<MonitorState>(
  initialState,
  on(
    actionMonitorAdmin,
    (state, { admin }): MonitorState => ({
      ...state,
      admin,
    })
  ),
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
    actionMonitorGroupsPageLoaded,
    (state, response): MonitorState => ({
      ...state,
      adminRole: response?.result?.adminRole === true,
      groupsPage: response,
    })
  ),
  on(
    actionMonitorGroupsPageDestroy,
    (state): MonitorState => ({
      ...state,
      groupsPage: undefined,
    })
  ),
  on(
    actionMonitorGroupPageLoaded,
    (state, response): MonitorState => ({
      ...state,
      adminRole: response?.result?.adminRole === true,
      groupName: response?.result?.groupName ?? state.groupName,
      groupDescription:
        response?.result?.groupDescription ?? state.groupDescription,
      groupPage: response,
    })
  ),
  on(
    actionMonitorGroupPageDestroy,
    (state): MonitorState => ({
      ...state,
      groupName: undefined,
      groupDescription: undefined,
      groupPage: undefined,
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
  on(
    actionMonitorNavigateGroup,
    (state, { groupName, groupDescription }): MonitorState => ({
      ...state,
      groupName,
      groupDescription,
    })
  ),
  on(
    actionMonitorGroupDeleteLoaded,
    (state, response): MonitorState => ({
      ...state,
      adminRole: response?.result?.adminRole === true,
      groupPage: response,
    })
  ),
  on(
    actionMonitorGroupDeleteDestroy,
    (state): MonitorState => ({
      ...state,
      groupPage: undefined,
    })
  ),
  on(
    actionMonitorGroupUpdateLoaded,
    (state, response): MonitorState => ({
      ...state,
      adminRole: response?.result?.adminRole === true,
      groupPage: response,
    })
  ),
  on(
    actionMonitorGroupUpdateDestroy,
    (state): MonitorState => ({
      ...state,
      groupPage: undefined,
    })
  ),
  on(
    actionMonitorRouteAddPageLoad,
    (state, { groupName }): MonitorState => ({
      ...state,
      groupName,
    })
  ),
  on(actionMonitorRouteAddPageLoaded, (state, response): MonitorState => {
    const groupName = response.result
      ? response.result.groupName
      : state.groupName;
    const groupDescription = response.result
      ? response.result.groupDescription
      : state.groupDescription;
    return {
      ...state,
      groupName,
      groupDescription,
      routeAddPage: response,
    };
  }),
  on(actionMonitorRouteAddPageDestroy, (state): MonitorState => {
    return {
      ...state,
      groupName: undefined,
      groupDescription: undefined,
      routeAddPage: undefined,
    };
  }),
  on(actionMonitorRouteUpdatePageLoaded, (state, response): MonitorState => {
    const result = response.result;
    const groupName = result?.groupName ?? state.groupName;
    const groupDescription = result?.groupDescription ?? state.groupDescription;
    const routeName = result?.routeName ?? state.routeName;
    const routeDescription = result?.routeDescription ?? state.routeDescription;
    return {
      ...state,
      groupName,
      groupDescription,
      routeName,
      routeDescription,
      routeUpdatePage: response,
    };
  }),
  on(actionMonitorRouteUpdatePageDestroy, (state): MonitorState => {
    return {
      ...state,
      groupName: undefined,
      groupDescription: undefined,
      routeName: undefined,
      routeDescription: undefined,
      routeUpdatePage: undefined,
    };
  }),
  on(actionMonitorRouteInfoLoaded, (state, response): MonitorState => {
    return {
      ...state,
      routeInfoPage: response,
    };
  }),
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
  on(actionMonitorRouteUploadInit, (state): MonitorState => {
    return {
      ...state,
      routeSaveState: {
        ...state.routeSaveState,
        saveRouteStatus: 'done',
        uploadGpxStatus: 'busy',
      },
    };
  }),
  on(actionMonitorRouteUploaded, (state): MonitorState => {
    return {
      ...state,
      routeSaveState: {
        ...state.routeSaveState,
        uploadGpxStatus: 'done',
        analyzeStatus: 'busy',
      },
    };
  }),
  on(actionMonitorRouteAnalyzed, (state): MonitorState => {
    return {
      ...state,
      routeSaveState: {
        ...state.routeSaveState,
        analyzeStatus: 'done',
        done: true,
      },
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
    actionMonitorRouteDetailsPageLoad,
    (state, { groupName, routeName }): MonitorState => {
      return {
        ...state,
        routeName,
        groupName,
      };
    }
  ),
  on(actionMonitorRouteDetailsPageLoaded, (state, response): MonitorState => {
    const result = response.result;
    const relationId = result?.relationId ?? state.relationId;
    const routeName = result?.routeName ?? state.routeName;
    const routeDescription = result?.routeDescription ?? state.routeDescription;
    const groupName = result?.groupName ?? state.groupName;
    const groupDescription = result?.groupDescription ?? state.groupDescription;
    return {
      ...state,
      relationId,
      routeName,
      routeDescription,
      groupName,
      groupDescription,
      routeDetailsPage: response,
    };
  }),
  on(actionMonitorRouteDetailsPageDestroy, (state): MonitorState => {
    return {
      ...state,
      relationId: undefined,
      routeName: undefined,
      routeDescription: undefined,
      groupName: undefined,
      groupDescription: undefined,
      routeDetailsPage: undefined,
    };
  }),
  on(
    actionMonitorRouteGpxPageLoad,
    (state, { groupName, routeName, relationId }): MonitorState => {
      return {
        ...state,
        routeName,
        groupName,
        relationId,
      };
    }
  ),
  on(actionMonitorRouteGpxPageLoaded, (state, response): MonitorState => {
    const result = response.result;
    const routeName = result?.routeName ?? state.routeName;
    const groupName = result?.groupName ?? state.groupName;
    return {
      ...state,
      routeName,
      groupName,
      routeGpxPage: response,
    };
  }),
  on(actionMonitorRouteGpxPageDestroy, (state): MonitorState => {
    return {
      ...state,
      relationId: undefined,
      routeName: undefined,
      routeDescription: undefined,
      groupName: undefined,
      groupDescription: undefined,
      routeGpxPage: undefined,
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
  }),
  on(actionMonitorRouteMapPageLoaded, (state, { page }): MonitorState => {
    const relationId = page?.relationId ?? state.relationId;
    const routeName = page?.routeName ?? state.routeName;
    const routeDescription = page?.routeDescription ?? state.routeDescription;
    const groupName = page?.groupName ?? state.groupName;
    const groupDescription = page?.groupDescription ?? state.groupDescription;

    return {
      ...state,
      relationId,
      routeName,
      routeDescription,
      groupName,
      groupDescription,
    };
  })
);
