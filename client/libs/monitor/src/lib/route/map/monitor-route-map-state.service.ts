import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { effect } from '@angular/core';
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
  readonly page = computed(() => this._state().page);
  readonly mode = computed(() => this._state().mode);
  readonly referenceVisible = computed(() => this._state().referenceVisible);
  readonly matchesVisible = computed(() => this._state().matchesVisible);
  readonly deviationsVisible = computed(() => this._state().deviationsVisible);
  readonly osmRelationVisible = computed(
    () => this._state().osmRelationVisible
  );
  readonly osmRelationAvailable = computed(
    () => this._state().osmRelationAvailable
  );
  readonly osmRelationEmpty = computed(() => this._state().osmRelationEmpty);
  readonly selectedDeviation = computed(() => this._state().selectedDeviation);
  readonly selectedOsmSegment = computed(
    () => this._state().selectedOsmSegment
  );

  readonly referenceType = computed(() => this._state().referenceType);
  readonly referenceAvailable = computed(
    () => this._state().referenceAvailable
  );
  readonly matchesEnabled = computed(() => this._state().matchesEnabled);
  readonly gpxDeviationsEnabled = computed(
    () => this._state().gpxDeviationsEnabled
  );
  readonly osmRelationEnabled = computed(
    () => this._state().osmRelationEnabled
  );
  readonly deviations = computed(() => this._state().deviations);
  readonly osmSegments = computed(() => this._state().osmSegments);

  readonly focus = this._focus.asReadonly();

  constructor() {
    effect(() => {
      console.log(['state', this._state()]);
    });
  }

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

    const osmRelationAvailable = !!page.relationId;

    const osmRelationEmpty = page.osmSegments.length === 0 && !!page.relationId;

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

    const referenceType = page.reference.referenceType;

    const referenceAvailable = (page.reference?.geoJson.length ?? 0) > 0;
    const referenceParam = queryParams['reference'];
    let referenceVisible =
      referenceAvailable &&
      !(matchesVisible || deviationsVisible || osmRelationVisible);
    if (referenceAvailable && referenceParam) {
      referenceVisible = referenceParam === 'true';
    }

    const matchesEnabled = mode === 'comparison' && !!page.matchesGeoJson;

    // TODO candidate for computed() and move to component?
    const gpxDeviationsEnabled =
      mode === MonitorMapMode.comparison && (page.deviations.length ?? 0) > 0;

    // TODO candidate for computed() and move to component?
    const osmRelationEnabled = (page.osmSegments.length ?? 0) > 0;

    // TODO candidate for computed() and move to component?
    const deviations = page.deviations;

    // TODO candidate for computed() and move to component?
    const osmSegments = page.osmSegments;

    const state: MonitorRouteMapState = {
      page,
      mode,
      referenceVisible,
      matchesVisible,
      deviationsVisible,
      osmRelationVisible,
      osmRelationAvailable,
      osmRelationEmpty,
      selectedDeviation,
      selectedOsmSegment,
      referenceType,
      referenceAvailable,
      matchesEnabled,
      gpxDeviationsEnabled,
      osmRelationEnabled,
      deviations,
      osmSegments,
    };
    this._state.set(state);
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
    this._focus.set(selectedDeviation.bounds);
  }

  selectedOsmSegmentChanged(selectedOsmSegment: MonitorRouteSegment): void {
    this._state.update((state) => ({ ...state, selectedOsmSegment }));
  }

  modeChanged(mode: MonitorMapMode): void {
    console.log(`mapModeChanged(${mode})`);
    const referenceVisible = false;
    let matchesVisible = false;
    let deviationsVisible = false;
    let osmRelationVisible = false;
    if (mode === MonitorMapMode.comparison) {
      matchesVisible = !!this.page()?.reference.geoJson;
      deviationsVisible = this.deviations().length > 0;
      osmRelationVisible = this.osmSegments().length > 0;
    } else if (mode === MonitorMapMode.osmSegments) {
      osmRelationVisible = true;
    }

    // TODO cleanup
    // this._mode.set(mode);
    // this._referenceVisible.set(referenceVisible);
    // this._matchesVisible.set(matchesVisible);
    // this._deviationsVisible.set(deviationsVisible);
    // this._osmRelationVisible.set(osmRelationVisible);
    // this._selectedDeviation.set(null);
    // this._selectedOsmSegment.set(null);
    //
    // this.osmRelationLayer.changed();
    //
    // this.updateQueryParams();
  }

  // TODO cleanup
  pageChanged(page: MonitorRouteMapPage): void {
    // this._referenceType.set(page.reference.referenceType);
    // this._referenceAvailable.set(!!page.reference.geoJson);
    // this._matchesEnabled.set(
    //   this.mode() === 'comparison' && !!page.matchesGeoJson
    // );
    // this._gpxDeviationsEnabled.set(
    //   this.mode() === MonitorMapMode.comparison &&
    //     (page.deviations.length ?? 0) > 0
    // );
    // this._osmRelationEnabled.set((page.osmSegments.length ?? 0) > 0);
    // this._deviations.set(page.deviations);
    // this._osmSegments.set(page.osmSegments);
  }

  focusChanged(bounds: Bounds): void {
    this._focus.set(bounds);
  }
}
