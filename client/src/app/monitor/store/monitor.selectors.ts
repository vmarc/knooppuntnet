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

export const selectMonitorRouteGroups = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeGroups
);

export const selectMonitorAdminRouteGroupPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.adminRouteGroupPage
);

export const selectMonitorRoutes = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routes
);

export const selectMonitorRouteDetails = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.details
);

export const selectMonitorRouteMap = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.map
);

export const selectMonitorRouteMapMode = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.mapMode
);

export const selectMonitorRouteMapBounds = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.map.result.bounds
);

export const selectMonitorRouteMapOsmSegments = createSelector(
  selectMonitorState,
  (state: MonitorState) => {
    if (state.map && state.map.result && state.map.result.osmSegments) {
      return state.map.result.osmSegments;
    }
    return [];
  }
);

export const selectMonitorRouteMapOsmSegmentCount = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.map?.result?.osmSegments?.length ?? 0
);

export const selectMonitorRouteMapNokSegments = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.map?.result?.nokSegments ?? []
);

export const selectMonitorRouteMapNokSegmentsCount = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.map?.result?.nokSegments?.length ?? 0
);

export const selectMonitorRouteChanges = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.changes
);

export const selectMonitorRouteChange = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.change
);

export const selectMonitorRouteChangesFiltered = createSelector(
  selectMonitorState,
  selectPreferencesImpact,
  (state: MonitorState, impact) => {
    const changes = state.changes?.result?.changes ?? [];
    return changes.filter(change => {
      if (impact) {
        return change.happy || change.investigate;
      }
      return true;
    });
  }
);

export const selectMonitorRouteId = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeId
);

export const selectMonitorRouteName = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeName
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
  (state: MonitorState) => !!state.map?.result?.gpxGeometry
);

export const selectMonitorRouteMapGpxOkEnabled = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.mapMode === 'comparison' && !!state.map?.result?.okGeometry
);

export const selectMonitorRouteMapGpxNokEnabled = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.mapMode === 'comparison' && (state.map?.result?.nokSegments?.length ?? 0) > 0
);

export const selectMonitorRouteMapOsmRelationEnabled = createSelector(
  selectMonitorState,
  (state: MonitorState) => (state.map?.result?.osmSegments?.length ?? 0) > 0
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