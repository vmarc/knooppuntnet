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
      pageResponse: undefined,
      mode: undefined,
      referenceVisible: undefined,
      matchesVisible: undefined,
      deviationsVisible: undefined,
      osmRelationVisible: undefined,
      mapPosition: undefined,
      pages: undefined,
      selectedDeviation: undefined,
      selectedOsmSegment: undefined,
    })
  ),
  on(
    actionMonitorRouteMapPageLoaded,
    (state, { response: pageResponse, queryParams }): MonitorRouteMapState => {
      const mapPage = pageResponse.result;
      let matchesVisible =
        !!mapPage?.matchesGeoJson && (mapPage?.osmSegments?.length ?? 0) > 0;
      if (matchesVisible && queryParams['matches']) {
        matchesVisible = queryParams['matches'] === 'true';
      }

      let deviationsVisible = (mapPage?.deviations?.length ?? 0) > 0;
      if (deviationsVisible && queryParams['deviations']) {
        deviationsVisible = queryParams['deviations'] === 'true';
      }

      let osmRelationVisible = (mapPage?.osmSegments?.length ?? 0) > 0;
      if (osmRelationVisible && queryParams['osm-relation']) {
        osmRelationVisible = queryParams['osm-relation'] === 'true';
      }

      const osmRelationAvailable = !!mapPage?.relationId;

      const osmRelationEmpty =
        (mapPage?.osmSegments?.length ?? 0) == 0 && !!mapPage?.relationId;

      const referenceAvailable = (mapPage?.reference?.geoJson.length ?? 0) > 0;
      let referenceVisible =
        referenceAvailable &&
        !(matchesVisible || deviationsVisible || osmRelationVisible);
      if (referenceAvailable && queryParams['reference']) {
        referenceVisible = queryParams['reference'] === 'true';
      }

      let mode = MonitorMapMode.comparison;
      if (queryParams['mode']) {
        if (queryParams['mode'] === 'osm-segments') {
          mode = MonitorMapMode.osmSegments;
        }
      }

      let selectedDeviation: MonitorRouteDeviation = null;
      const selectedDeviationParameter = queryParams['selected-deviation'];
      console.log(
        'selectedDeviation query param = ' + selectedDeviationParameter
      );

      if (!isNaN(Number(selectedDeviationParameter))) {
        const id = +selectedDeviationParameter;
        const selected = pageResponse?.result?.deviations?.find(
          (d) => d.id === id
        );
        if (selected) {
          selectedDeviation = selected;
        }
      }

      let selectedOsmSegment: MonitorRouteSegment = null;
      const selectedOsmSegmentParam = queryParams['selected-osm-segment'];
      if (!isNaN(Number(selectedOsmSegmentParam))) {
        const id = +selectedOsmSegmentParam;
        const selected = pageResponse?.result?.osmSegments?.find(
          (segment) => segment.id === id
        );
        if (selected) {
          selectedOsmSegment = selected;
        }
      }

      let pages = state.pages;
      if (mapPage.currentSubRelation) {
        if (!pages) {
          pages = new Map<number, MonitorRouteMapPage>();
        }
        pages = pages.set(mapPage.currentSubRelation.relationId, mapPage);
      }

      return {
        ...state,
        referenceVisible,
        matchesVisible,
        deviationsVisible,
        osmRelationVisible,
        osmRelationAvailable,
        osmRelationEmpty,
        mode,
        selectedDeviation,
        selectedOsmSegment,
        pages,
        pageResponse,
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
        selectedDeviation: deviation,
      };
    }
  ),
  on(
    actionMonitorRouteMapSelectOsmSegment,
    (state, segment): MonitorRouteMapState => {
      return {
        ...state,
        selectedOsmSegment: segment,
      };
    }
  ),
  on(
    actionMonitorRouteMapMode,
    (state, { mapMode: mode }): MonitorRouteMapState => {
      const referenceVisible = false;
      let matchesVisible = false;
      let deviationsVisible = false;
      let osmRelationVisible = false;
      if (mode === MonitorMapMode.comparison) {
        matchesVisible = !!state.pageResponse?.result?.reference.geoJson;
        deviationsVisible =
          (state.pageResponse.result?.deviations?.length ?? 0) > 0;
        osmRelationVisible =
          (state.pageResponse.result?.osmSegments?.length ?? 0) > 0;
      } else if (mode === MonitorMapMode.osmSegments) {
        osmRelationVisible = true;
      }

      return {
        ...state,
        mode,
        referenceVisible,
        matchesVisible,
        deviationsVisible,
        osmRelationVisible,
        selectedDeviation: null,
        selectedOsmSegment: null,
      };
    }
  ),
  on(
    actionMonitorRouteMapReferenceVisible,
    (state, { visible }): MonitorRouteMapState => ({
      ...state,
      referenceVisible: visible,
    })
  ),
  on(
    actionMonitorRouteMapMatchesVisible,
    (state, { visible }): MonitorRouteMapState => ({
      ...state,
      matchesVisible: visible,
    })
  ),
  on(
    actionMonitorRouteMapDeviationsVisible,
    (state, { visible }): MonitorRouteMapState => ({
      ...state,
      deviationsVisible: visible,
    })
  ),
  on(
    actionMonitorRouteMapOsmRelationVisible,
    (state, { visible }): MonitorRouteMapState => ({
      ...state,
      osmRelationVisible: visible,
    })
  )
);
