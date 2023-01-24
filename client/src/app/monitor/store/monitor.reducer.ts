import { MonitorRouteDeviation } from '@api/common/monitor/monitor-route-deviation';
import { MonitorRouteMapPage } from '@api/common/monitor/monitor-route-map-page';
import { MonitorRouteSegment } from '@api/common/monitor/monitor-route-segment';
import { createReducer } from '@ngrx/store';
import { on } from '@ngrx/store';
import { MonitorMapMode } from '../route/map/monitor-map-mode';
import { actionMonitorRouteAddPageLoad } from './monitor.actions';
import { actionMonitorRouteDetailsPageLoad } from './monitor.actions';
import { actionMonitorRouteMapPageLoad } from './monitor.actions';
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
    actionMonitorRouteMapPageDestroy,
    (state): MonitorState => ({
      ...state,
      routeMapPage: undefined,
      mapMode: undefined,
      mapReferenceVisible: undefined,
      mapMatchesVisible: undefined,
      mapDeviationsVisible: undefined,
      mapOsmRelationVisible: undefined,
      mapPosition: undefined,
      mapPages: undefined,
      routeMapSelectedDeviation: undefined,
      routeMapSelectedOsmSegment: undefined,
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
    actionMonitorRouteMapPageLoad,
    (state, { groupName, routeName }): MonitorState => {
      return {
        ...state,
        routeName,
        groupName,
      };
    }
  ),
  on(
    actionMonitorRouteMapPageLoaded,
    (state, { response, queryParams }): MonitorState => {
      const mapPage = response.result;
      const relationId = mapPage?.relationId ?? state.relationId;
      const routeName = mapPage?.routeName ?? state.routeName;
      const routeDescription =
        mapPage?.routeDescription ?? state.routeDescription;
      const groupName = mapPage?.groupName ?? state.groupName;
      const groupDescription =
        mapPage?.groupDescription ?? state.groupDescription;

      let mapMatchesVisible =
        !!mapPage?.matchesGeoJson && (mapPage?.osmSegments?.length ?? 0) > 0;
      if (mapMatchesVisible && queryParams['matches']) {
        mapMatchesVisible = queryParams['matches'] === 'true';
      }

      let mapDeviationsVisible = (mapPage?.deviations?.length ?? 0) > 0;
      if (mapDeviationsVisible && queryParams['deviations']) {
        mapDeviationsVisible = queryParams['deviations'] === 'true';
      }

      let mapOsmRelationVisible = (mapPage?.osmSegments?.length ?? 0) > 0;
      if (mapOsmRelationVisible && queryParams['osm-relation']) {
        mapOsmRelationVisible = queryParams['osm-relation'] === 'true';
      }

      const mapOsmRelationAvailable = !!mapPage?.relationId;

      const mapOsmRelationEmpty =
        (mapPage?.osmSegments?.length ?? 0) == 0 && !!mapPage?.relationId;

      const referenceAvailable = (mapPage?.reference?.geoJson.length ?? 0) > 0;
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

      let mapPages = state.mapPages;
      if (mapPage.currentSubRelation) {
        if (!mapPages) {
          mapPages = new Map<number, MonitorRouteMapPage>();
        }
        mapPages = mapPages.set(mapPage.currentSubRelation.relationId, mapPage);
      }

      return {
        ...state,
        relationId,
        routeName,
        routeDescription,
        groupName,
        groupDescription,
        mapReferenceVisible,
        mapMatchesVisible,
        mapDeviationsVisible,
        mapOsmRelationVisible,
        mapOsmRelationAvailable,
        mapOsmRelationEmpty,
        mapMode,
        routeMapSelectedDeviation,
        routeMapSelectedOsmSegment,
        mapPages,
        routeMapPage: response,
      };
    }
  ),
  on(
    actionMonitorRouteMapPositionChanged,
    (state, { mapPosition }): MonitorState => {
      return {
        ...state,
        mapPosition,
      };
    }
  ),
  on(actionMonitorRouteMapSelectDeviation, (state, deviation): MonitorState => {
    return {
      ...state,
      routeMapSelectedDeviation: deviation,
    };
  }),
  on(actionMonitorRouteMapSelectOsmSegment, (state, segment): MonitorState => {
    return {
      ...state,
      routeMapSelectedOsmSegment: segment,
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
  on(actionMonitorRouteChangesPageDestroy, (state, response): MonitorState => {
    return {
      ...state,
      routeId: undefined,
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
      routeId,
      routeName,
      routeChangePage: response,
    };
  }),
  on(actionMonitorRouteChangePageDestroy, (state, response): MonitorState => {
    return {
      ...state,
      routeId: undefined,
      routeName: undefined,
      routeChangePage: undefined,
    };
  }),
  on(actionMonitorRouteMapMode, (state, { mapMode }): MonitorState => {
    const mapReferenceVisible = false;
    let mapMatchesVisible = false;
    let mapDeviationsVisible = false;
    let mapOsmRelationVisible = false;
    if (mapMode === MonitorMapMode.comparison) {
      mapMatchesVisible = !!state.routeMapPage?.result?.reference.geoJson;
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
  on(
    actionMonitorRouteMapReferenceVisible,
    (state, { visible }): MonitorState => ({
      ...state,
      mapReferenceVisible: visible,
    })
  ),
  on(
    actionMonitorRouteMapMatchesVisible,
    (state, { visible }): MonitorState => ({
      ...state,
      mapMatchesVisible: visible,
    })
  ),
  on(
    actionMonitorRouteMapDeviationsVisible,
    (state, { visible }): MonitorState => ({
      ...state,
      mapDeviationsVisible: visible,
    })
  ),
  on(
    actionMonitorRouteMapOsmRelationVisible,
    (state, { visible }): MonitorState => ({
      ...state,
      mapOsmRelationVisible: visible,
    })
  )
);
