import { MonitorRouteDeviation } from '@api/common/monitor/monitor-route-deviation';
import { MonitorRouteMapPage } from '@api/common/monitor/monitor-route-map-page';
import { MonitorRouteSegment } from '@api/common/monitor/monitor-route-segment';
import { createReducer } from '@ngrx/store';
import { on } from '@ngrx/store';
import { MonitorMapMode } from '../monitor-map-mode';
import { actionMonitorRouteMapPageDestroy } from './monitor-route-map.actions';
import { actionMonitorRouteMapPositionChanged } from './monitor-route-map.actions';
import { actionMonitorRouteMapSelectOsmSegment } from './monitor-route-map.actions';
import { actionMonitorRouteMapSelectDeviation } from './monitor-route-map.actions';
import { actionMonitorRouteMapReferenceVisible } from './monitor-route-map.actions';
import { actionMonitorRouteMapMatchesVisible } from './monitor-route-map.actions';
import { actionMonitorRouteMapOsmRelationVisible } from './monitor-route-map.actions';
import { actionMonitorRouteMapDeviationsVisible } from './monitor-route-map.actions';
import { actionMonitorRouteMapMode } from './monitor-route-map.actions';
import { actionMonitorRouteMapPageLoaded } from './monitor-route-map.actions';
import { MonitorRouteMapState } from './monitor-route-map.state';
import { initialState } from './monitor-route-map.state';

export const monitorRouteMapReducer = createReducer<MonitorRouteMapState>(
  initialState,
  on(
    actionMonitorRouteMapPageDestroy,
    (state): MonitorRouteMapState => ({
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
    actionMonitorRouteMapPageLoaded,
    (state, { response, queryParams }): MonitorRouteMapState => {
      const mapPage = response.result;
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
    (state, { mapPosition }): MonitorRouteMapState => {
      return {
        ...state,
        mapPosition,
      };
    }
  ),
  on(
    actionMonitorRouteMapSelectDeviation,
    (state, deviation): MonitorRouteMapState => {
      return {
        ...state,
        routeMapSelectedDeviation: deviation,
      };
    }
  ),
  on(
    actionMonitorRouteMapSelectOsmSegment,
    (state, segment): MonitorRouteMapState => {
      return {
        ...state,
        routeMapSelectedOsmSegment: segment,
      };
    }
  ),
  on(actionMonitorRouteMapMode, (state, { mapMode }): MonitorRouteMapState => {
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
    (state, { visible }): MonitorRouteMapState => ({
      ...state,
      mapReferenceVisible: visible,
    })
  ),
  on(
    actionMonitorRouteMapMatchesVisible,
    (state, { visible }): MonitorRouteMapState => ({
      ...state,
      mapMatchesVisible: visible,
    })
  ),
  on(
    actionMonitorRouteMapDeviationsVisible,
    (state, { visible }): MonitorRouteMapState => ({
      ...state,
      mapDeviationsVisible: visible,
    })
  ),
  on(
    actionMonitorRouteMapOsmRelationVisible,
    (state, { visible }): MonitorRouteMapState => ({
      ...state,
      mapOsmRelationVisible: visible,
    })
  )
);
