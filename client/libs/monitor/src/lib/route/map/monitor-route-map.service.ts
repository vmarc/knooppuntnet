import { effect } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { Params } from '@angular/router';
import { Bounds } from '@api/common';
import { MonitorRouteSegment } from '@api/common/monitor';
import { MonitorRouteDeviation } from '@api/common/monitor';
import { MonitorRouteMapPage } from '@api/common/monitor';
import { MapPosition } from '@app/components/ol/domain';
import { ZoomLevel } from '@app/components/ol/domain';
import { BackgroundLayer } from '@app/components/ol/layers';
import { MapControls } from '@app/components/ol/layers';
import { MapLayerRegistry } from '@app/components/ol/layers';
import { OsmLayer } from '@app/components/ol/layers';
import { OpenlayersMapService } from '@app/components/ol/services';
import { Util } from '@app/components/shared';
import { Coordinate } from 'ol/coordinate';
import { GeoJSON } from 'ol/format';
import { Geometry } from 'ol/geom';
import VectorLayer from 'ol/layer/Vector';
import Map from 'ol/Map';
import VectorSource from 'ol/source/Vector';
import { Stroke } from 'ol/style';
import { Style } from 'ol/style';
import View from 'ol/View';
import { MonitorMapMode } from './monitor-map-mode';

@Injectable()
export class MonitorRouteMapService extends OpenlayersMapService {
  private readonly _page = signal<MonitorRouteMapPage | null>(null);

  private readonly _mode = signal<MonitorMapMode>(MonitorMapMode.comparison);
  private readonly _referenceVisible = signal(false);
  private readonly _matchesVisible = signal(false);
  private readonly _deviationsVisible = signal(false);
  private readonly _osmRelationVisible = signal(false);
  private readonly _osmRelationAvailable = signal(false);
  private readonly _osmRelationEmpty = signal(false);
  // private readonly _pages: Map<number, MonitorRouteMapPage> | undefined;
  // private readonly _page: MonitorRouteMapPage | undefined;
  private readonly _selectedDeviation = signal<MonitorRouteDeviation | null>(
    null
  );
  private readonly _selectedOsmSegment = signal<MonitorRouteSegment | null>(
    null
  );

  private readonly _referenceType = signal('osm');
  private readonly _referenceAvailable = signal(false);
  private readonly _matchesEnabled = signal(false);
  private readonly _gpxDeviationsEnabled = signal(false);
  private readonly _osmRelationEnabled = signal(false);
  private readonly _deviations = signal<MonitorRouteDeviation[]>([]);
  private readonly _osmSegments = signal<MonitorRouteSegment[]>([]);

  readonly page = this._page.asReadonly();
  readonly mode = this._mode.asReadonly();
  readonly referenceVisible = this._referenceVisible.asReadonly();
  readonly matchesVisible = this._matchesVisible.asReadonly();
  readonly deviationsVisible = this._deviationsVisible.asReadonly();
  readonly osmRelationVisible = this._osmRelationVisible.asReadonly();
  readonly osmRelationAvailable = this._osmRelationAvailable.asReadonly();
  readonly osmRelationEmpty = this._osmRelationEmpty.asReadonly();
  // readonly pages= this._mode.asReadonly();
  // readonly page= this._mode.asReadonly();
  readonly selectedDeviation = this._selectedDeviation.asReadonly();
  readonly selectedOsmSegment = this._selectedOsmSegment.asReadonly();

  readonly referenceType = this._referenceType.asReadonly();
  readonly referenceAvailable = this._referenceAvailable.asReadonly();
  readonly matchesEnabled = this._matchesEnabled.asReadonly();
  readonly gpxDeviationsEnabled = this._gpxDeviationsEnabled.asReadonly();
  readonly osmRelationEnabled = this._osmRelationEnabled.asReadonly();
  readonly deviations = this._deviations.asReadonly();
  readonly osmSegments = this._osmSegments.asReadonly();

  private readonly colors = [
    'red',
    'yellow',
    'lime',
    'aqua',
    'green',
    'teal',
    'blue',
    'fuchsia',
    'olive',
    'purple',
    'teal',
  ];

  private readonly osmSegmentStyles = this.colors.map((color) =>
    this.fixedStyle(color, 4)
  );

  private readonly referenceLayer: VectorLayer<VectorSource<Geometry>>;
  private readonly matchesLayer: VectorLayer<VectorSource<Geometry>>;
  private readonly deviationsLayer: VectorLayer<VectorSource<Geometry>>;
  private readonly osmRelationLayer: VectorLayer<VectorSource<Geometry>>;

  constructor() {
    console.log('MonitorRouteMapService.constructor');
    super();
    effect(() => {
      console.log('EFFECT referenceVisible=' + this.referenceVisible());
    });

    this.referenceLayer = this.buildReferencesLayer();
    this.matchesLayer = this.buildMatchesLayer();
    this.deviationsLayer = this.buildDeviationsLayer();
    this.osmRelationLayer = this.buildOsmRelationLayer();
    this.initialize();
  }

  init(params: Params, queryParams: Params): void {
    const param = queryParams['position'];
    const mapPositionFromUrl = MapPosition.fromQueryParam(param);

    const matchesParam = queryParams['matches'];
    let matchesVisible =
      !!this.page().matchesGeoJson && this.page().osmSegments.length > 0;
    if (matchesVisible && matchesParam) {
      matchesVisible = matchesParam === 'true';
    }
    this._matchesVisible.set(matchesVisible);

    const deviationsParam = queryParams['deviations'];
    let deviationsVisible = this.page().deviations.length > 0;
    if (deviationsVisible && deviationsParam) {
      deviationsVisible = deviationsParam === 'true';
    }
    this._deviationsVisible.set(deviationsVisible);

    const osmRelationParam = queryParams['osm-relation'];
    let osmRelationVisible = this.page().osmSegments.length > 0;
    if (osmRelationVisible && osmRelationParam) {
      osmRelationVisible = osmRelationParam === 'true';
    }
    this._osmRelationVisible.set(osmRelationVisible);

    this._osmRelationEnabled.set(!!this.page().relationId);

    const osmRelationEmpty =
      this.page().osmSegments.length === 0 && !!this.page().relationId;
    this._osmRelationEmpty.set(osmRelationEmpty);

    const referenceAvailable = (this.page().reference?.geoJson.length ?? 0) > 0;
    const referenceParam = queryParams['reference'];
    let referenceVisible =
      referenceAvailable &&
      !(matchesVisible || deviationsVisible || osmRelationVisible);
    if (referenceAvailable && referenceParam) {
      referenceVisible = referenceParam === 'true';
    }
    this._referenceAvailable.set(referenceAvailable);
    this._referenceVisible.set(referenceVisible);

    let mode = MonitorMapMode.comparison;
    const modeParam = queryParams['mode'];
    if (modeParam) {
      if (modeParam === 'osm-segments') {
        mode = MonitorMapMode.osmSegments;
      }
    }
    this._mode.set(mode);

    const selectedDeviationParameter = queryParams['selected-deviation'];
    if (!isNaN(Number(selectedDeviationParameter))) {
      const id = +selectedDeviationParameter;
      const selectedDeviation = this.page().deviations?.find(
        (d) => d.id === id
      );
      if (selectedDeviation) {
        this._selectedDeviation.set(selectedDeviation);
      }
    }

    const selectedOsmSegmentParam = queryParams['selected-osm-segment'];
    if (!isNaN(Number(selectedOsmSegmentParam))) {
      const id = +selectedOsmSegmentParam;
      const selectedOsmSegment = this.page().osmSegments.find(
        (segment) => segment.id === id
      );
      if (selectedOsmSegment) {
        this._selectedOsmSegment.set(selectedOsmSegment);
      }
    }

    this.registerLayers();
    this.initMap(
      new Map({
        target: this.mapId,
        layers: this.layers,
        controls: MapControls.build(),
        view: new View({
          minZoom: 0,
          maxZoom: ZoomLevel.vectorTileMaxOverZoom,
        }),
      })
    );

    this.map.addLayer(this.referenceLayer);
    this.map.addLayer(this.matchesLayer);
    this.map.addLayer(this.deviationsLayer);
    this.map.addLayer(this.osmRelationLayer);

    if (mapPositionFromUrl) {
      this.map.getView().setZoom(mapPositionFromUrl.zoom);
      this.map.getView().setRotation(mapPositionFromUrl.rotation);
      const center: Coordinate = [mapPositionFromUrl.x, mapPositionFromUrl.y];
      this.map.getView().setCenter(center);
    } else {
      this.map.getView().fit(Util.toExtent(this.page().bounds, 0.05));
    }
    this.finalizeSetup(true);
  }

  referenceVisibleChanged(visible: boolean): void {
    this._referenceVisible.set(visible);
    this.updateQueryParams();
  }

  matchesVisibleChanged(value: boolean): void {
    this._matchesVisible.set(value);
    this.updateQueryParams();
  }

  deviationsVisibleChanged(value: boolean): void {
    this._deviationsVisible.set(value);
    this.updateQueryParams();
  }

  osmRelationVisibleChanged(value: boolean): void {
    this._osmRelationVisible.set(value);
    this.updateQueryParams();
  }

  selectedDeviationChanged(deviation: MonitorRouteDeviation): void {
    this._selectedDeviation.set(deviation);
  }

  selectedOsmSegmentChanged(osmSegment: MonitorRouteSegment): void {
    this._selectedOsmSegment.set(osmSegment);
  }

  mapModeChanged(mode: MonitorMapMode): void {
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

    this._mode.set(mode);
    this._referenceVisible.set(referenceVisible);
    this._matchesVisible.set(matchesVisible);
    this._deviationsVisible.set(deviationsVisible);
    this._osmRelationVisible.set(osmRelationVisible);
    this._selectedDeviation.set(null);
    this._selectedOsmSegment.set(null);

    this.osmRelationLayer.changed();

    this.updateQueryParams();
  }

  pageChanged(page: MonitorRouteMapPage): void {
    this._referenceType.set(page.reference.referenceType);
    this._referenceAvailable.set(!!page.reference.geoJson);
    this._matchesEnabled.set(
      this.mode() === 'comparison' && !!page.matchesGeoJson
    );
    this._gpxDeviationsEnabled.set(
      this.mode() === MonitorMapMode.comparison &&
        (page.deviations.length ?? 0) > 0
    );
    this._osmRelationEnabled.set((page.osmSegments.length ?? 0) > 0);
    this._deviations.set(page.deviations);
    this._osmSegments.set(page.osmSegments);

    this.referenceLayer.getSource().clear();
    if (page?.reference?.geoJson) {
      const features = new GeoJSON().readFeatures(page.reference.geoJson, {
        featureProjection: 'EPSG:3857',
      });
      this.referenceLayer.getSource().addFeatures(features);
    }

    this.matchesLayer.getSource().clear();
    if (page?.matchesGeoJson) {
      const features = new GeoJSON().readFeatures(page.matchesGeoJson, {
        featureProjection: 'EPSG:3857',
      });
      this.matchesLayer.getSource().addFeatures(features);
    }

    this.deviationsLayer.getSource().clear();
    if (page?.deviations) {
      const features = [];
      page.deviations.forEach((segment) => {
        new GeoJSON()
          .readFeatures(segment.geoJson, { featureProjection: 'EPSG:3857' })
          .forEach((feature) => features.push(feature));
      });
      this.deviationsLayer.getSource().addFeatures(features);
    }

    this.osmRelationLayer.getSource().clear();
    if (page?.osmSegments) {
      const features = [];
      page.osmSegments.forEach((segment) => {
        new GeoJSON()
          .readFeatures(segment.geoJson, { featureProjection: 'EPSG:3857' })
          .forEach((feature) => {
            feature.set('segmentId', segment.id);
            features.push(feature);
          });
      });
      this.osmRelationLayer.getSource().addFeatures(features);
    }
    console.log('MonitorRouteMapService setting page');
    this._page.set(page);
  }

  focus(bounds: Bounds): void {
    if (this.map !== null && bounds) {
      this.map.getView().fit(Util.toExtent(bounds, 0.1));
    }
  }

  colorForSegmentId(id: number): string {
    const index = id % 10;
    return this.colors[index];
  }

  private styleForSegmentId(id: number): Style {
    const index = id % 10;
    return this.osmSegmentStyles[index];
  }

  private registerLayers(): void {
    const registry = new MapLayerRegistry();
    registry.register([], BackgroundLayer.build(), true);
    registry.register([], OsmLayer.build(), false);
    this.register(registry);
  }

  private initialize(): void {
    effect(() => {
      this.referenceLayer.setVisible(this.referenceVisible());
    });
    effect(() => {
      this.matchesLayer.setVisible(this.matchesVisible());
    });
    effect(() => {
      this.deviationsLayer.setVisible(this.deviationsVisible());
    });
    effect(() => {
      this.osmRelationLayer.setVisible(this.osmRelationVisible());
    });
    effect(() => {
      const deviation = this.selectedDeviation();
      if (deviation) {
        this.focus(deviation.bounds);
      }
    });
  }

  private buildReferencesLayer(): VectorLayer<VectorSource<Geometry>> {
    const layerStyle = this.fixedStyle('blue', 4);
    return new VectorLayer({
      zIndex: 50,
      source: new VectorSource(),
      style: () => layerStyle,
    });
  }

  private buildMatchesLayer(): VectorLayer<VectorSource<Geometry>> {
    const layerStyle = this.fixedStyle('green', 4);
    return new VectorLayer({
      zIndex: 60,
      source: new VectorSource(),
      style: () => layerStyle,
    });
  }

  private buildDeviationsLayer(): VectorLayer<VectorSource<Geometry>> {
    const layerStyle = this.fixedStyle('red', 4);
    return new VectorLayer({
      zIndex: 70,
      source: new VectorSource(),
      style: () => layerStyle,
    });
  }

  private buildOsmRelationLayer(): VectorLayer<VectorSource<Geometry>> {
    const thinStyle = this.fixedStyle('gold', 4);
    const thickStyle = this.fixedStyle('gold', 10);

    const styleFunction = (feature) => {
      if (this.mode() === 'osm-segments') {
        const segmentId = feature.get('segmentId');
        return this.styleForSegmentId(segmentId);
      }
      if (this.referenceAvailable()) {
        return thickStyle;
      }
      return thinStyle;
    };

    return new VectorLayer({
      zIndex: 40,
      source: new VectorSource(),
      style: styleFunction,
    });
  }

  private fixedStyle(color: string, width: number): Style {
    return new Style({
      stroke: new Stroke({
        color,
        width,
      }),
    });
  }

  private updateQueryParams(): void {
    let selectedDeviation = 0;
    if (this.selectedDeviation()) {
      selectedDeviation = this.selectedDeviation().id;
    }
    let selectedOsmSegment = 0;
    if (this.selectedOsmSegment()) {
      selectedOsmSegment = this.selectedOsmSegment().id;
    }
    const subRelationId = this.page().currentSubRelation?.relationId ?? 0;

    const queryParams: Params = {
      mode: this.mode(),
      reference: this.referenceVisible(),
      matches: this.matchesVisible(),
      deviations: this.deviationsVisible(),
      'osm-relation': this.osmRelationVisible(),
      'selected-deviation': selectedDeviation,
      'selected-osm-segment': selectedOsmSegment,
      'sub-relation-id': subRelationId,
    };

    this.setQueryParams(queryParams);
  }
}
