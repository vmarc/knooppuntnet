import {createFeatureSelector} from '@ngrx/store';
import {createSelector} from '@ngrx/store';
import {selectPreferencesImpact} from '../../core/preferences/preferences.selectors';
import {monitorFeatureKey} from './monitor.state';
import {MonitorRootState} from './monitor.state';
import {MonitorState} from './monitor.state';

export const selectMonitorState = createFeatureSelector<MonitorRootState, MonitorState>(monitorFeatureKey);

export const selectMonitorAdmin = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.admin
);

export const selectMonitorChangesPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.changesPage
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

export const selectMonitorAdminGroupPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.adminGroupPage
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
    if (state.routeMapPage && state.routeMapPage.result && state.routeMapPage.result.osmSegments) {
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

export const selectMonitorRouteChangesPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeChangesPage
);

export const selectMonitorRouteChangePage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeChangePage
);

export const selectMonitorRouteId = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeId
);

export const selectMonitorRouteName = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeName
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
  (state: MonitorState) => state.mapMode === 'comparison' && !!state.routeMapPage?.result?.okGeometry
);

export const selectMonitorRouteMapGpxNokEnabled = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.mapMode === 'comparison' && (state.routeMapPage?.result?.nokSegments?.length ?? 0) > 0
);

export const selectMonitorRouteMapOsmRelationEnabled = createSelector(
  selectMonitorState,
  (state: MonitorState) => (state.routeMapPage?.result?.osmSegments?.length ?? 0) > 0
);

/*************************************/

export const selectLongdistanceRoutesPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.longdistanceRoutesPage
);

export const selectLongdistanceRouteDetailsPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.longdistanceRouteDetailsPage
);

export const selectLongdistanceRouteMapPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.longdistanceRouteMapPage
);

export const selectLongdistanceRouteMapMode = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.longdistanceRouteMapMode
);

export const selectLongdistanceRouteMapBounds = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.longdistanceRouteMapPage.result.bounds
);

export const selectLongdistanceRouteMapOsmSegments = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.longdistanceRouteMapPage?.result?.osmSegments ?? []
);

export const selectLongdistanceRouteMapOsmSegmentCount = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.longdistanceRouteMapPage?.result?.osmSegments?.length ?? 0
);

export const selectLongdistanceRouteMapNokSegments = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.longdistanceRouteMapPage?.result?.nokSegments ?? []
);

export const selectLongdistanceRouteMapNokSegmentsCount = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.longdistanceRouteMapPage?.result?.nokSegments?.length ?? 0
);

export const selectLongdistanceRouteChangesPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.longdistanceRouteChangesPage
);

export const selectLongdistanceRouteChangePage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.longdistanceRouteChangePage
);

export const selectLongdistanceRouteChangesFiltered = createSelector(
  selectMonitorState,
  selectPreferencesImpact,
  (state: MonitorState, impact) => {
    const changes = state.longdistanceRouteChangesPage?.result?.changes ?? [];
    return changes.filter(change => {
      if (impact) {
        return change.happy || change.investigate;
      }
      return true;
    });
  }
);

export const selectLongdistanceRouteId = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.longdistanceRouteId
);

export const selectLongdistanceRouteName = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.longdistanceRouteName
);

export const selectLongdistanceRouteMapGpxVisible = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.longdistanceRouteMapGpxVisible
);

export const selectLongdistanceRouteMapGpxOkVisible = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.longdistanceRouteMapGpxOkVisible
);

export const selectLongdistanceRouteMapGpxNokVisible = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.longdistanceRouteMapGpxNokVisible
);

export const selectLongdistanceRouteMapOsmRelationVisible = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.longdistanceRouteMapOsmRelationVisible
);

export const selectLongdistanceRouteMapGpxEnabled = createSelector(
  selectMonitorState,
  (state: MonitorState) => !!state.longdistanceRouteMapPage?.result?.gpxGeometry
);

export const selectLongdistanceRouteMapGpxOkEnabled = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.longdistanceRouteMapMode === 'comparison' && !!state.longdistanceRouteMapPage?.result?.okGeometry
);

export const selectLongdistanceRouteMapGpxNokEnabled = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.longdistanceRouteMapMode === 'comparison' && (state.longdistanceRouteMapPage?.result?.nokSegments?.length ?? 0) > 0
);

export const selectLongdistanceRouteMapOsmRelationEnabled = createSelector(
  selectMonitorState,
  (state: MonitorState) => (state.longdistanceRouteMapPage?.result?.osmSegments?.length ?? 0) > 0
);
