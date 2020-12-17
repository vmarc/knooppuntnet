import {createSelector} from '@ngrx/store';
import {selectMonitorState} from '../core.state';
import {selectPreferencesImpact} from '../preferences/preferences.selectors';
import {MonitorState} from './monitor.state';

export const selectMonitorAdmin = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.admin
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
    return changes.filter( change => {
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
