import {routerNavigationAction} from '@ngrx/router-store';
import {createReducer} from '@ngrx/store';
import {on} from '@ngrx/store';
import {actionLongDistanceRouteMapGpxVisible} from './long-distance.actions';
import {actionLongDistanceRouteMapGpxOkVisible} from './long-distance.actions';
import {actionLongDistanceRouteMapOsmRelationVisible} from './long-distance.actions';
import {actionLongDistanceRouteMapGpxNokVisible} from './long-distance.actions';
import {actionLongDistanceRouteMapMode} from './long-distance.actions';
import {actionLongDistanceRouteMapFocus} from './long-distance.actions';
import {actionLongDistanceRoutesLoaded} from './long-distance.actions';
import {actionLongDistanceRouteChangesLoaded} from './long-distance.actions';
import {actionLongDistanceRouteMapLoaded} from './long-distance.actions';
import {actionLongDistanceRouteDetailsLoaded} from './long-distance.actions';
import {initialState} from './long-distance.state';

export const longDistanceReducer = createReducer(
  initialState,
  on(
    routerNavigationAction,
    (state, action) => ({
      ...state,
      routes: null,
      details: null,
      map: null,
      changes: null
    })
  ),
  on(
    actionLongDistanceRoutesLoaded,
    (state, {response}) => {
      return {
        ...state,
        routes: response
      };
    }
  ),
  on(
    actionLongDistanceRouteDetailsLoaded,
    (state, {response}) => {
      const routeId = response.result?.id ?? state.routeId;
      const routeName = response.result?.name ?? state.routeName;
      return {
        ...state,
        routeId,
        routeName,
        details: response
      };
    }
  ),
  on(
    actionLongDistanceRouteMapLoaded,
    (state, {response}) => {
      const routeId = response.result?.id ?? state.routeId;
      const routeName = response.result?.name ?? state.routeName;
      const mapGpxVisible = false;
      const mapGpxOkVisible = !!(response.result?.okGeometry);
      const mapGpxNokVisible = !!(response.result?.nokSegments);
      const mapOsmRelationVisible = !!(response.result?.osmSegments);
      return {
        ...state,
        routeId,
        routeName,
        mapGpxVisible,
        mapGpxOkVisible,
        mapGpxNokVisible,
        mapOsmRelationVisible,
        mapMode: 'comparison',
        map: response
      };
    }
  ),
  on(
    actionLongDistanceRouteChangesLoaded,
    (state, {response}) => {
      const routeId = response.result?.id ?? state.routeId;
      const routeName = response.result?.name ?? state.routeName;
      return {
        ...state,
        routeId,
        routeName,
        changes: response
      };
    }
  ),
  on(
    actionLongDistanceRouteMapMode,
    (state, {mode}) => {

      let mapGpxVisible = false;
      let mapGpxOkVisible = false;
      let mapGpxNokVisible = false;
      let mapOsmRelationVisible = false;
      if (mode === 'comparison') {
        mapGpxOkVisible = !!(state.map?.result?.gpxGeometry);
        mapGpxNokVisible = !!(state.map?.result?.nokSegments);
        mapOsmRelationVisible = !!(state.map?.result?.osmSegments);
      } else if (mode === 'osm-segments') {
        mapOsmRelationVisible = true;
      }

      return {
        ...state,
        mapGpxVisible,
        mapGpxOkVisible,
        mapGpxNokVisible,
        mapOsmRelationVisible,
        mapMode: mode
      };
    }
  ),
  on(
    actionLongDistanceRouteMapFocus,
    (state, {segmentId, bounds}) => {
      return {
        ...state,
        mapFocusNokSegmentId: segmentId,
        mapFocus: bounds
      };
    }
  ),
  on(
    actionLongDistanceRouteMapGpxVisible,
    (state, {visible}) => {
      return {
        ...state,
        mapGpxVisible: visible
      };
    }
  ),
  on(
    actionLongDistanceRouteMapGpxOkVisible,
    (state, {visible}) => {
      return {
        ...state,
        mapGpxOkVisible: visible
      };
    }
  ),
  on(
    actionLongDistanceRouteMapGpxNokVisible,
    (state, {visible}) => {
      return {
        ...state,
        mapGpxNokVisible: visible
      };
    }
  ),
  on(
    actionLongDistanceRouteMapOsmRelationVisible,
    (state, {visible}) => {
      return {
        ...state,
        mapOsmRelationVisible: visible
      };
    }
  )
);
