import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { Params } from '@angular/router';
import { Bounds } from '@api/common';
import { MonitorRouteSegment } from '@api/common/monitor';
import { MonitorRouteDeviation } from '@api/common/monitor';
import { MonitorRouteMapPage } from '@api/common/monitor';
import { MonitorMapMode } from './monitor-map-mode';
import { initialState } from './monitor-route-map-state';
import { MonitorRouteMapState } from './monitor-route-map-state';

@Injectable()
export class MonitorRouteMapStateService {
  private readonly _state = signal<MonitorRouteMapState>(initialState);
  private readonly _focus = signal<Bounds | null>(null);

  readonly state = this._state.asReadonly();
  readonly focus = this._focus.asReadonly();

  readonly page = computed(() => {
    return this._state().page;
  });
  readonly mode = computed(() => {
    return this._state().mode;
  });
  readonly referenceVisible = computed(() => {
    return this._state().referenceVisible;
  });
  readonly matchesVisible = computed(() => {
    return this._state().matchesVisible;
  });
  readonly deviationsVisible = computed(() => {
    return this._state().deviationsVisible;
  });
  readonly osmRelationVisible = computed(() => {
    return this._state().osmRelationVisible;
  });
  readonly selectedDeviation = computed(() => {
    return this._state().selectedDeviation;
  });
  readonly selectedOsmSegment = computed(
    () => this._state().selectedOsmSegment
  );
  readonly referenceAvailable = computed(() => {
    return this._state().referenceAvailable;
  });

  initialState(
    params: Params,
    queryParams: Params,
    page: MonitorRouteMapPage
  ): void {
    let mode = MonitorMapMode.comparison;
    const modeParam = queryParams['mode'];
    if (modeParam) {
      if (modeParam === 'osm-segments') {
        mode = MonitorMapMode.osmSegments;
      }
    }

    const matchesParam = queryParams['matches'];
    let matchesVisible = !!page.matchesGeoJson && page.osmSegments.length > 0;
    if (matchesVisible && matchesParam) {
      matchesVisible = matchesParam === 'true';
    }

    const deviationsParam = queryParams['deviations'];
    let deviationsVisible = page.deviations.length > 0;
    if (deviationsVisible && deviationsParam) {
      deviationsVisible = deviationsParam === 'true';
    }

    const osmRelationParam = queryParams['osm-relation'];
    let osmRelationVisible = page.osmSegments.length > 0;
    if (osmRelationVisible && osmRelationParam) {
      osmRelationVisible = osmRelationParam === 'true';
    }

    let selectedDeviation: MonitorRouteDeviation = null;
    const selectedDeviationParameter = queryParams['selected-deviation'];
    if (!isNaN(Number(selectedDeviationParameter))) {
      const id = +selectedDeviationParameter;
      selectedDeviation = page.deviations?.find((d) => d.id === id);
    }

    let selectedOsmSegment: MonitorRouteSegment = null;
    const selectedOsmSegmentParam = queryParams['selected-osm-segment'];
    if (!isNaN(Number(selectedOsmSegmentParam))) {
      const id = +selectedOsmSegmentParam;
      selectedOsmSegment = page.osmSegments.find(
        (segment) => segment.id === id
      );
    }

    const referenceAvailable =
      (page.reference?.referenceGeoJson.length ?? 0) > 0;
    const referenceParam = queryParams['reference'];
    let referenceVisible =
      referenceAvailable &&
      !(matchesVisible || deviationsVisible || osmRelationVisible);
    if (referenceAvailable && referenceParam) {
      referenceVisible = referenceParam === 'true';
    }

    const state: MonitorRouteMapState = {
      page,
      mode,
      referenceVisible,
      matchesVisible,
      deviationsVisible,
      osmRelationVisible,
      selectedDeviation,
      selectedOsmSegment,
      referenceAvailable,
    };
    this._state.set(state);
  }

  pageChanged(page: MonitorRouteMapPage): void {
    this._state.update((state) => ({ ...state, page }));
  }

  referenceVisibleChanged(referenceVisible: boolean): void {
    this._state.update((state) => ({ ...state, referenceVisible }));
  }

  matchesVisibleChanged(matchesVisible: boolean): void {
    this._state.update((state) => ({ ...state, matchesVisible }));
  }

  deviationsVisibleChanged(deviationsVisible: boolean): void {
    this._state.update((state) => ({ ...state, deviationsVisible }));
  }

  osmRelationVisibleChanged(osmRelationVisible: boolean): void {
    this._state.update((state) => ({ ...state, osmRelationVisible }));
  }

  selectedDeviationChanged(selectedDeviation: MonitorRouteDeviation): void {
    this._state.update((state) => ({ ...state, selectedDeviation }));
    if (selectedDeviation) {
      this._focus.set(selectedDeviation.bounds);
    }
  }

  selectedOsmSegmentChanged(selectedOsmSegment: MonitorRouteSegment): void {
    this._state.update((state) => ({ ...state, selectedOsmSegment }));
  }

  modeChanged(mode: MonitorMapMode): void {
    this._state.update((state) => ({ ...state, mode }));
  }

  focusChanged(bounds: Bounds): void {
    this._focus.set(bounds);
  }
}
