import { createFeatureSelector } from '@ngrx/store';
import { createSelector } from '@ngrx/store';
import { MonitorMapMode } from '../route/map/monitor-map-mode';
import { monitorFeatureKey } from './monitor.state';
import { MonitorState } from './monitor.state';

export const selectMonitorState =
  createFeatureSelector<MonitorState>(monitorFeatureKey);

export const selectMonitorAdmin = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.admin
);

export const selectMonitorAdminRole = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.adminRole
);

export const selectMonitorChangesPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.changesPage
);

export const selectMonitorChangesPageIndex = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.changesPageIndex
);

export const selectMonitorGroupsPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.groupsPage
);

export const selectMonitorGroupPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.groupPage
);

export const selectMonitorGroupChangesPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.groupChangesPage
);

export const selectMonitorGroupChangesPageIndex = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.groupChangesPageIndex
);

export const selectMonitorRouteAddPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeAddPage
);

export const selectMonitorRouteUpdatePage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeUpdatePage
);

export const selectMonitorRouteInfoPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeInfoPage
);

export const selectMonitorRouteSaveState = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeSaveState
);

export const selectMonitorRouteDetailsPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeDetailsPage
);

export const selectMonitorRouteMapPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeMapPage
);

export const selectMonitorRouteMapMode = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.mapMode
);

export const selectMonitorRouteMapBounds = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeMapPage.result.bounds
);

export const selectMonitorRouteMapOsmSegments = createSelector(
  selectMonitorState,
  (state: MonitorState) => {
    if (
      state.routeMapPage &&
      state.routeMapPage.result &&
      state.routeMapPage.result.osmSegments
    ) {
      return state.routeMapPage.result.osmSegments;
    }
    return [];
  }
);

export const selectMonitorRouteMapOsmSegmentCount = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeMapPage?.result?.osmSegments?.length ?? 0
);

export const selectMonitorRouteMapDeviations = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeMapPage?.result?.deviations ?? []
);

export const selectMonitorRouteMapSelectedDeviation = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeMapSelectedDeviation
);

export const selectMonitorRouteMapSelectedDeviationDisabled = createSelector(
  selectMonitorState,
  (state: MonitorState) =>
    state.mapMode !== MonitorMapMode.comparison ||
    !state.routeMapSelectedDeviation
);

export const selectMonitorRouteMapSelectedOsmSegment = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeMapSelectedOsmSegment
);

export const selectMonitorRouteMapSelectedOsmSegmentDisabled = createSelector(
  selectMonitorState,
  (state: MonitorState) =>
    state.mapMode !== MonitorMapMode.osmSegments ||
    !state.routeMapSelectedOsmSegment
);

export const selectMonitorRouteChangesPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeChangesPage
);

export const selectMonitorRouteChangesPageIndex = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeChangesPageIndex
);

export const selectMonitorRouteChangePage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeChangePage
);

export const selectMonitorRelationId = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.relationId
);

export const selectMonitorRouteName = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeName
);

export const selectMonitorRouteDescription = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeDescription
);

export const selectMonitorGroupName = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.groupName
);

export const selectMonitorGroupDescription = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.groupDescription
);

export const selectMonitorRouteMapReferenceVisible = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.mapReferenceVisible
);

export const selectMonitorRouteMapMatchesVisible = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.mapMatchesVisible
);

export const selectMonitorRouteMapDeviationsVisible = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.mapDeviationsVisible
);

export const selectMonitorRouteMapOsmRelationVisible = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.mapOsmRelationVisible
);

export const selectMonitorRouteMapOsmRelationAvailable = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.mapOsmRelationAvailable
);

export const selectMonitorRouteMapOsmRelationEmpty = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.mapOsmRelationEmpty
);

export const selectMonitorRouteMapReferenceEnabled = createSelector(
  selectMonitorState,
  (state: MonitorState) => !!state.routeMapPage?.result?.reference?.geoJson
);

export const selectMonitorRouteMapMatchesEnabled = createSelector(
  selectMonitorState,
  (state: MonitorState) =>
    state.mapMode === 'comparison' &&
    !!state.routeMapPage?.result?.matchesGeoJson
);

export const selectMonitorRouteMapDeviationsEnabled = createSelector(
  selectMonitorState,
  (state: MonitorState) =>
    state.mapMode === MonitorMapMode.comparison &&
    (state.routeMapPage?.result?.deviations?.length ?? 0) > 0
);

export const selectMonitorRouteMapOsmRelationEnabled = createSelector(
  selectMonitorState,
  (state: MonitorState) =>
    (state.routeMapPage?.result?.osmSegments?.length ?? 0) > 0
);
