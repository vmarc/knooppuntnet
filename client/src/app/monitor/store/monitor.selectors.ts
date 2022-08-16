import { createFeatureSelector } from '@ngrx/store';
import { createSelector } from '@ngrx/store';
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

export const selectMonitorGroupsPageHasGroups = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.groupsPage.result?.groups.length > 0
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

export const selectMonitorRouteInfoPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeInfoPage
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

export const selectMonitorRouteMapNokSegments = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeMapPage?.result?.nokSegments ?? []
);

export const selectMonitorRouteMapNokSegmentsCount = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeMapPage?.result?.nokSegments?.length ?? 0
);

export const selectMonitorRouteMapSelectedDeviation = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeMapSelectedDeviation
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

export const selectMonitorMonitorRouteId = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.monitorRouteId
);

export const selectMonitorRouteId = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeId
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

export const selectMonitorRouteMapGpxVisible = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.mapGpxVisible
);

export const selectMonitorRouteMapGpxOkVisible = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.mapGpxOkVisible
);

export const selectMonitorRouteMapGpxNokVisible = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.mapGpxNokVisible
);

export const selectMonitorRouteMapOsmRelationVisible = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.mapOsmRelationVisible
);

export const selectMonitorRouteMapGpxEnabled = createSelector(
  selectMonitorState,
  (state: MonitorState) => !!state.routeMapPage?.result?.reference.geometry
);

export const selectMonitorRouteMapGpxOkEnabled = createSelector(
  selectMonitorState,
  (state: MonitorState) =>
    state.mapMode === 'comparison' && !!state.routeMapPage?.result?.okGeometry
);

export const selectMonitorRouteMapGpxNokEnabled = createSelector(
  selectMonitorState,
  (state: MonitorState) =>
    state.mapMode === 'comparison' &&
    (state.routeMapPage?.result?.nokSegments?.length ?? 0) > 0
);

export const selectMonitorRouteMapOsmRelationEnabled = createSelector(
  selectMonitorState,
  (state: MonitorState) =>
    (state.routeMapPage?.result?.osmSegments?.length ?? 0) > 0
);
