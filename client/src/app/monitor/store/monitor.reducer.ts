import { MonitorRouteDeviation } from '@api/common/monitor/monitor-route-deviation';
import { MonitorRouteSegment } from '@api/common/monitor/monitor-route-segment';
import { routerNavigationAction } from '@ngrx/router-store';
import { createReducer } from '@ngrx/store';
import { on } from '@ngrx/store';
import { MonitorMapMode } from '../route/map/monitor-map-mode';
import { actionMonitorRouteMapPageDestroy } from './monitor.actions';
import { actionMonitorRouteMapPositionChanged } from './monitor.actions';
import { actionMonitorRouteMapSelectOsmSegment } from './monitor.actions';
import { actionMonitorRouteSaved } from './monitor.actions';
import { actionMonitorRouteUploadInit } from './monitor.actions';
import { actionMonitorRouteUploaded } from './monitor.actions';
import { actionMonitorRouteAnalyzed } from './monitor.actions';
import { actionMonitorRouteSaveInit } from './monitor.actions';
import { actionMonitorRouteAdminRelationIdChanged } from './monitor.actions';
import { actionMonitorRouteUpdatePageLoaded } from './monitor.actions';
import { actionMonitorRouteAddPageLoaded } from './monitor.actions';
import { actionMonitorRouteMapSelectDeviation } from './monitor.actions';
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
import { actionMonitorRouteMapReferenceVisible } from './monitor.actions';
import { actionMonitorRouteMapMatchesVisible } from './monitor.actions';
import { actionMonitorRouteMapOsmRelationVisible } from './monitor.actions';
import { actionMonitorRouteMapDeviationsVisible } from './monitor.actions';
import { actionMonitorRouteMapMode } from './monitor.actions';
import { actionMonitorRouteChangesPageLoaded } from './monitor.actions';
import { actionMonitorRouteMapPageLoaded } from './monitor.actions';
import { actionMonitorRouteDetailsPageLoaded } from './monitor.actions';
import { MonitorRouteSaveState } from './monitor.state';
import { initialState } from './monitor.state';

export const monitorReducer = createReducer(
  initialState,
  on(actionMonitorAdmin, (state, { admin }) => ({
    ...state,
    admin,
  })),
  on(routerNavigationAction, (state) => ({
    ...state,
    routeGroups: null,
    changesPage: null,
    groupsPage: null,
    groupPage: null,
    groupChangesPage: null,
    routeDetailsPage: null,
    routeAddPage: null,
    routeUpdatePage: null,
    routeInfoPage: null,
    routeSaveState: null,
    // routeMapPage: null, should not set routeMapPage to null when changing the url query parameters
    routeChangesPage: null,
    routeChangePage: null,
  })),
  on(actionMonitorRouteMapPageDestroy, (state) => ({
    ...state,
    routeMapPage: null,
    mapMode: null,
    mapReferenceVisible: null,
    mapMatchesVisible: null,
    mapDeviationsVisible: null,
    mapOsmRelationVisible: null,
    mapPosition: null,
  })),
  on(actionMonitorChangesPageInit, (state) => ({
    ...state,
    changesPageIndex: 0,
  })),
  on(actionMonitorChangesPageLoaded, (state, response) => ({
    ...state,
    changesPage: response,
  })),
  on(actionMonitorChangesPageIndex, (state, action) => ({
    ...state,
    changesPageIndex: action.pageIndex,
  })),
  on(actionMonitorGroupsPageLoaded, (state, response) => ({
    ...state,
    adminRole: response?.result?.adminRole === true,
    groupsPage: response,
  })),
  on(actionMonitorGroupPageLoaded, (state, response) => ({
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
  on(actionMonitorGroupChangesPageLoaded, (state, response) => ({
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
  on(actionMonitorGroupDeleteLoaded, (state, response) => ({
    ...state,
    adminRole: response?.result?.adminRole === true,
    groupPage: response,
  })),
  on(actionMonitorGroupUpdateLoaded, (state, response) => ({
    ...state,
    adminRole: response?.result?.adminRole === true,
    groupPage: response,
  })),
  on(actionMonitorRouteAddPageLoaded, (state, response) => {
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
  on(actionMonitorRouteUpdatePageLoaded, (state, response) => {
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
  on(actionMonitorRouteInfoLoaded, (state, response) => {
    return {
      ...state,
      routeInfoPage: response,
    };
  }),
  on(actionMonitorRouteAdminRelationIdChanged, (state) => {
    return {
      ...state,
      routeInfoPage: null,
    };
  }),
  on(actionMonitorRouteSaveInit, (state, parameters) => {
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
        parameters.properties.gpxFileChanged;
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
  on(actionMonitorRouteUploadInit, (state) => {
    return {
      ...state,
      routeSaveState: {
        ...state.routeSaveState,
        saveRouteStatus: 'done',
        uploadGpxStatus: 'busy',
      },
    };
  }),
  on(actionMonitorRouteUploaded, (state) => {
    return {
      ...state,
      routeSaveState: {
        ...state.routeSaveState,
        uploadGpxStatus: 'done',
        analyzeStatus: 'busy',
      },
    };
  }),
  on(actionMonitorRouteAnalyzed, (state) => {
    return {
      ...state,
      routeSaveState: {
        ...state.routeSaveState,
        analyzeStatus: 'done',
        done: true,
      },
    };
  }),
  on(actionMonitorRouteSaved, (state, response) => {
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
  on(actionMonitorRouteDetailsPageLoaded, (state, response) => {
    const result = response.result;
    const routeId = result?.routeId ?? state.routeId;
    const relationId = result?.relationId ?? state.relationId;
    const routeName = result?.routeName ?? state.routeName;
    const routeDescription = result?.routeDescription ?? state.routeDescription;
    const groupName = result?.groupName ?? state.groupName;
    const groupDescription = result?.groupDescription ?? state.groupDescription;
    return {
      ...state,
      routeId,
      relationId,
      routeName,
      routeDescription,
      groupName,
      groupDescription,
      routeDetailsPage: response,
    };
  }),
  on(actionMonitorRouteMapPageLoaded, (state, { response, queryParams }) => {
    const result = response.result;
    const routeId = result?.routeId ?? state.routeId;
    const relationId = result?.relationId ?? state.relationId;
    const routeName = result?.routeName ?? state.routeName;
    const routeDescription = result?.routeDescription ?? state.routeDescription;
    const groupName = result?.groupName ?? state.groupName;
    const groupDescription = result?.groupDescription ?? state.groupDescription;

    let mapMatchesVisible = !!result?.matchesGeometry;
    if (mapMatchesVisible && queryParams['matches']) {
      mapMatchesVisible = queryParams['matches'] === 'true';
    }

    let mapDeviationsVisible = (result?.deviations?.length ?? 0) > 0;
    if (mapDeviationsVisible && queryParams['deviations']) {
      mapDeviationsVisible = queryParams['deviations'] === 'true';
    }

    let mapOsmRelationVisible = (result?.osmSegments?.length ?? 0) > 0;
    if (mapOsmRelationVisible && queryParams['osm-relation']) {
      mapOsmRelationVisible = queryParams['osm-relation'] === 'true';
    }

    const referenceAvailable = (result?.reference?.geometry.length ?? 0) > 0;
    let mapReferenceVisible =
      referenceAvailable &&
      !(mapMatchesVisible || mapDeviationsVisible || mapOsmRelationVisible);
    if (referenceAvailable && queryParams['reference']) {
      mapReferenceVisible = queryParams['reference'] === 'true';
    }

    let mapMode = MonitorMapMode.comparison;
    if (queryParams['mode']) {
      if (queryParams['mode'] === 'osm-segments') {
        mapMode = MonitorMapMode.osmSegments;
      }
    }

    let routeMapSelectedDeviation: MonitorRouteDeviation = null;
    const selectedDeviation = queryParams['selected-deviation'];
    console.log('selectedDeviation query param = ' + selectedDeviation);

    if (!isNaN(Number(selectedDeviation))) {
      const id = +selectedDeviation;
      const selected = response?.result?.deviations?.find((d) => d.id === id);
      if (selected) {
        routeMapSelectedDeviation = selected;
        console.log(
          '   routeMapSelectedDeviation= ' +
            JSON.stringify(routeMapSelectedDeviation)
        );
      } else {
        console.log('   routeMapSelectedDeviation= NOTHING SELECTED');
      }
    }

    let routeMapSelectedOsmSegment: MonitorRouteSegment = null;
    const selectedOsmSegmentParam = queryParams['selected-osm-segment'];
    if (!isNaN(Number(selectedOsmSegmentParam))) {
      const id = +selectedOsmSegmentParam;
      const selected = response?.result?.osmSegments?.find(
        (segment) => segment.id === id
      );
      if (selected) {
        routeMapSelectedOsmSegment = selected;
      }
    }

    return {
      ...state,
      routeId,
      relationId,
      routeName,
      routeDescription,
      groupName,
      groupDescription,
      mapReferenceVisible,
      mapMatchesVisible,
      mapDeviationsVisible,
      mapOsmRelationVisible,
      mapMode,
      routeMapSelectedDeviation,
      routeMapSelectedOsmSegment,
      routeMapPage: response,
    };
  }),
  on(actionMonitorRouteMapPositionChanged, (state, { mapPosition }) => {
    return {
      ...state,
      mapPosition,
    };
  }),
  on(actionMonitorRouteMapSelectDeviation, (state, deviation) => {
    return {
      ...state,
      routeMapSelectedDeviation: deviation,
    };
  }),
  on(actionMonitorRouteMapSelectOsmSegment, (state, segment) => {
    return {
      ...state,
      routeMapSelectedOsmSegment: segment,
    };
  }),
  on(actionMonitorRouteChangesPageInit, (state) => ({
    ...state,
    routeChangesPageIndex: 0,
  })),
  on(actionMonitorRouteChangesPageLoaded, (state, response) => {
    const result = response.result;
    const routeId = result?.routeId ?? state.routeId;
    const routeName = result?.routeName ?? state.routeName;
    const groupName = result?.groupName ?? state.groupName;
    const groupDescription = result?.groupDescription ?? state.groupDescription;
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
  on(actionMonitorRouteChangePageLoaded, (state, response) => {
    const routeId = 'TODO MON'; // response.result?.key.elementId ?? state.routeId;
    const routeName = 'ROUTE-NAME'; // response.result?.name ?? state.routeName;
    return {
      ...state,
      routeId,
      routeName,
      routeChangePage: response,
    };
  }),
  on(actionMonitorRouteMapMode, (state, { mapMode }) => {
    const mapReferenceVisible = false;
    let mapMatchesVisible = false;
    let mapDeviationsVisible = false;
    let mapOsmRelationVisible = false;
    if (mapMode === MonitorMapMode.comparison) {
      mapMatchesVisible = !!state.routeMapPage?.result?.reference.geometry;
      mapDeviationsVisible =
        (state.routeMapPage.result?.deviations?.length ?? 0) > 0;
      mapOsmRelationVisible =
        (state.routeMapPage.result?.osmSegments?.length ?? 0) > 0;
    } else if (mapMode === MonitorMapMode.osmSegments) {
      mapOsmRelationVisible = true;
    }

    return {
      ...state,
      mapMode,
      mapReferenceVisible,
      mapMatchesVisible,
      mapDeviationsVisible,
      mapOsmRelationVisible,
      routeMapSelectedDeviation: null,
      routeMapSelectedOsmSegment: null,
    };
  }),
  on(actionMonitorRouteMapReferenceVisible, (state, { visible }) => ({
    ...state,
    mapReferenceVisible: visible,
  })),
  on(actionMonitorRouteMapMatchesVisible, (state, { visible }) => ({
    ...state,
    mapMatchesVisible: visible,
  })),
  on(actionMonitorRouteMapDeviationsVisible, (state, { visible }) => ({
    ...state,
    mapDeviationsVisible: visible,
  })),
  on(actionMonitorRouteMapOsmRelationVisible, (state, { visible }) => ({
    ...state,
    mapOsmRelationVisible: visible,
  }))
);
