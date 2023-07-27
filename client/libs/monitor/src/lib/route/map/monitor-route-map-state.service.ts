import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { ParamMap } from '@angular/router';
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

  readonly page = computed(() => this._state().page);
  readonly mode = computed(() => this._state().mode);
  readonly referenceVisible = computed(() => this._state().referenceVisible);
  readonly matchesVisible = computed(() => this._state().matchesVisible);
  readonly deviationsVisible = computed(() => this._state().deviationsVisible);
  readonly osmRelationVisible = computed(
    () => this._state().osmRelationVisible
  );
  readonly selectedDeviation = computed(() => this._state().selectedDeviation);
  readonly selectedOsmSegment = computed(
    () => this._state().selectedOsmSegment
  );
  readonly referenceAvailable = computed(
    () => this._state().referenceAvailable
  );
  readonly referenceLayerVisible = computed(
    () => this.mode() === MonitorMapMode.comparison && this.referenceVisible()
  );
  readonly matchesLayerVisible = computed(
    () => this.mode() === MonitorMapMode.comparison && this.matchesVisible()
  );
  readonly deviationsLayerVisible = computed(
    () => this.mode() === MonitorMapMode.comparison && this.deviationsVisible()
  );
  readonly osmRelationLayerVisible = computed(
    () =>
      this.mode() === MonitorMapMode.osmSegments ||
      (this.mode() === MonitorMapMode.comparison && this.osmRelationVisible())
  );

  initialState(queryParams: ParamMap, page: MonitorRouteMapPage): void {
    let mode = MonitorMapMode.comparison;
    const modeParam = queryParams.get('mode');
    if (modeParam === MonitorMapMode.osmSegments) {
      mode = MonitorMapMode.osmSegments;
    }

    const matchesParam = queryParams.get('matches');
    let matchesVisible = !!page.matchesGeoJson && page.osmSegments.length > 0;
    if (matchesVisible && matchesParam) {
      matchesVisible = matchesParam === 'true';
    }

    const deviationsParam = queryParams.get('deviations');
    let deviationsVisible = page.deviations.length > 0;
    if (deviationsVisible && deviationsParam) {
      deviationsVisible = deviationsParam === 'true';
    }

    const osmRelationParam = queryParams.get('osm-relation');
    let osmRelationVisible = page.osmSegments.length > 0;
    if (osmRelationVisible && osmRelationParam) {
      osmRelationVisible = osmRelationParam === 'true';
    }

    let selectedDeviation: MonitorRouteDeviation = null;
    const selectedDeviationParameter = queryParams.get('selected-deviation');
    if (!isNaN(Number(selectedDeviationParameter))) {
      const id = +selectedDeviationParameter;
      selectedDeviation = page.deviations?.find((d) => d.id === id);
    }

    let selectedOsmSegment: MonitorRouteSegment = null;
    const selectedOsmSegmentParam = queryParams.get('selected-osm-segment');
    if (!isNaN(Number(selectedOsmSegmentParam))) {
      const id = +selectedOsmSegmentParam;
      selectedOsmSegment = page.osmSegments.find(
        (segment) => segment.id === id
      );
    }

    const referenceAvailable =
      (page.reference?.referenceGeoJson.length ?? 0) > 0;
    const referenceParam = queryParams.get('reference');
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
    if (selectedOsmSegment) {
      this._focus.set(selectedOsmSegment.bounds);
    }
  }

  modeChanged(mode: MonitorMapMode): void {
    this._state.update((state) => ({ ...state, mode }));
  }

  focusChanged(bounds: Bounds): void {
    this._focus.set(bounds);
  }
}
