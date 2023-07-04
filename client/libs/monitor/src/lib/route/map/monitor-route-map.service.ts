import { inject } from '@angular/core';
import { effect } from '@angular/core';
import { Injectable } from '@angular/core';
import { Params } from '@angular/router';
import { MonitorRouteMapPage } from '@api/common/monitor';
import { MapPosition } from '@app/components/ol/domain';
import { ZoomLevel } from '@app/components/ol/domain';
import { BackgroundLayer } from '@app/components/ol/layers';
import { MapControls } from '@app/components/ol/layers';
import { MapLayerRegistry } from '@app/components/ol/layers';
import { OsmLayer } from '@app/components/ol/layers';
import { OpenlayersMapService } from '@app/components/ol/services';
import { NavService } from '@app/components/shared';
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
import { MonitorRouteMapState } from './monitor-route-map-state';
import { MonitorRouteMapStateService } from './monitor-route-map-state.service';

@Injectable()
export class MonitorRouteMapService extends OpenlayersMapService {
  private readonly navService = inject(NavService);
  private readonly stateService = inject(MonitorRouteMapStateService);

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
    super();
    this.referenceLayer = this.buildReferencesLayer();
    this.matchesLayer = this.buildMatchesLayer();
    this.deviationsLayer = this.buildDeviationsLayer();
    this.osmRelationLayer = this.buildOsmRelationLayer();
    this.initEffects();
  }

  init(): void {
    const param = this.navService.queryParam('position');
    const mapPositionFromUrl = MapPosition.fromQueryParam(param);

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
      this.map
        .getView()
        .fit(Util.toExtent(this.stateService.page().bounds, 0.05));
    }
    this.finalizeSetup(true);
  }

  pageChanged(page: MonitorRouteMapPage): void {
    this.referenceLayer.getSource().clear();
    if (page?.reference?.referenceGeoJson) {
      const features = new GeoJSON().readFeatures(
        page.reference.referenceGeoJson,
        {
          featureProjection: 'EPSG:3857',
        }
      );
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

  private initEffects(): void {
    effect(() => {
      this.referenceLayer.setVisible(this.stateService.referenceVisible());
    });
    effect(() => {
      this.matchesLayer.setVisible(this.stateService.matchesVisible());
    });
    effect(() => {
      this.deviationsLayer.setVisible(this.stateService.deviationsVisible());
    });
    effect(() => {
      this.osmRelationLayer.setVisible(this.stateService.osmRelationVisible());
    });
    effect(() => {
      const focusBounds = this.stateService.focus();
      if (this.map !== null && focusBounds !== null) {
        this.map.getView().fit(Util.toExtent(focusBounds, 0.1));
      }
    });
    effect(
      () => {
        if (this.stateService.page() !== null) {
          this.updateQueryParams(this.stateService.state());
        }
      },
      {
        allowSignalWrites: true,
      }
    );
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
      if (this.stateService.mode() === 'osm-segments') {
        const segmentId = feature.get('segmentId');
        return this.styleForSegmentId(segmentId);
      }
      if (this.stateService.referenceAvailable()) {
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

  private updateQueryParams(state: MonitorRouteMapState): void {
    let queryParams: Params = {
      mode: state.mode,
      reference: state.referenceVisible,
      matches: state.matchesVisible,
      deviations: state.deviationsVisible,
      'osm-relation': state.osmRelationVisible,
    };

    if (state.selectedDeviation) {
      queryParams = {
        ...queryParams,
        'selected-deviation': state.selectedDeviation.id,
      };
    }

    if (state.selectedOsmSegment) {
      queryParams = {
        ...queryParams,
        'selected-osm-segment': state.selectedOsmSegment.id,
      };
    }

    const subRelationId = state.page?.currentSubRelation?.relationId;
    if (subRelationId) {
      queryParams = {
        ...queryParams,
        'sub-relation-id': subRelationId,
      };
    }

    this.setQueryParams(queryParams);
  }
}
