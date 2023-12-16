import { inject } from '@angular/core';
import { effect } from '@angular/core';
import { Injectable } from '@angular/core';
import { Params } from '@angular/router';
import { MonitorRouteMapPage } from '@api/common/monitor';
import { MapPosition } from '@app/ol/domain';
import { ZoomLevel } from '@app/ol/domain';
import { TileDebug256Layer } from '@app/ol/layers';
import { NetworkMarkerLayer } from '@app/ol/layers';
import { BackgroundLayer } from '@app/ol/layers';
import { MapControls } from '@app/ol/layers';
import { MapLayerRegistry } from '@app/ol/layers';
import { OsmLayer } from '@app/ol/layers';
import { OpenlayersMapService } from '@app/ol/services';
import { NavService } from '@app/components/shared';
import { Util } from '@app/components/shared';
import { MapBrowserEvent } from 'ol';
import { Coordinate } from 'ol/coordinate';
import { FeatureLike } from 'ol/Feature';
import { GeoJSON } from 'ol/format';
import { Geometry } from 'ol/geom';
import Interaction from 'ol/interaction/Interaction';
import VectorLayer from 'ol/layer/Vector';
import Map from 'ol/Map';
import MapBrowserEventType from 'ol/MapBrowserEventType';
import VectorSource from 'ol/source/Vector';
import { Stroke } from 'ol/style';
import { Style } from 'ol/style';
import View from 'ol/View';
import { MonitorLayer } from '../../../ol/layers/monitor-layer';
import { MonitorMapMode } from './monitor-map-mode';
import { MonitorRouteMapState } from './monitor-route-map-state';
import { MonitorRouteMapStateService } from './monitor-route-map-state.service';

@Injectable()
export class MonitorRouteMapService extends OpenlayersMapService {
  private readonly navService = inject(NavService);
  private readonly stateService = inject(MonitorRouteMapStateService);

  private readonly colors = [
    '#e6194B', // red
    '#3cb44b', // green
    '#ffe119', // yellow
    '#4363d8', // blue
    '#f58231', // orange
    '#911eb4', // purple
    '#42d4f4', // cyan
    '#f032e6', // magenta
    '#bfef45', // lime
    '#fabed4', // pink
    '#469990', // teal
    '#dcbeff', // lavender
    '#9A6324', // brown
    '#fffac8', // beige
    '#800000', // maroon
    '#aaffc3', // mint
    '#808000', // olive
    '#ffd8b1', // apricot
    '#000075', // navy
  ];

  private readonly osmSegmentStyles = this.colors.map((color) => this.fixedStyle(color, 4));

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
      this.map.getView().fit(Util.toExtent(this.stateService.page().bounds, 0.05));
    }

    this.map.addInteraction(this.buildInteraction());

    this.finalizeSetup(true);
  }

  pageChanged(page: MonitorRouteMapPage): void {
    this.referenceLayer.getSource().clear();
    if (page?.reference?.referenceGeoJson) {
      const features = new GeoJSON().readFeatures(page.reference.referenceGeoJson, {
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
  }

  colorForSegmentId(id: number): string {
    const index = (id - 1) % this.colors.length;
    return this.colors[index];
  }

  private styleForSegmentId(id: number): Style {
    const index = (id - 1) % this.colors.length;
    return this.osmSegmentStyles[index];
  }

  private registerLayers(): void {
    const registry = new MapLayerRegistry();
    registry.register([], BackgroundLayer.build(), true);
    registry.register([], OsmLayer.build(), false);
    // registry.register([], MonitorLayer.build(), true);
    // registry.register([], TileDebug256Layer.build(), false);
    this.register(registry);
  }

  private initEffects(): void {
    effect(() => {
      const visible = this.stateService.referenceLayerVisible();
      this.referenceLayer.setVisible(visible);
    });
    effect(() => {
      const visible = this.stateService.matchesLayerVisible();
      this.matchesLayer.setVisible(visible);
    });
    effect(() => {
      const visible = this.stateService.deviationsLayerVisible();
      this.deviationsLayer.setVisible(visible);
    });
    effect(() => {
      const visible = this.stateService.osmRelationLayerVisible();
      this.osmRelationLayer.setVisible(visible);
    });
    effect(() => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const mode = this.stateService.mode();
      this.osmRelationLayer.changed();
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
      if (this.stateService.mode() === MonitorMapMode.osmSegments) {
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
    const queryParams: Params = {
      mode: state.mode,
      reference: state.referenceVisible,
      matches: state.matchesVisible,
      deviations: state.deviationsVisible,
      'osm-relation': state.osmRelationVisible,
      'selected-deviation': state.selectedDeviation?.id,
      'selected-osm-segment': state.selectedOsmSegment?.id,
      'sub-relation-id': state.page?.currentSubRelation?.relationId,
    };
    this.setQueryParams(queryParams);
  }

  private oldRrelationIdString = '';

  private buildInteraction(): Interaction {
    return new Interaction({
      handleEvent: (event: MapBrowserEvent<MouseEvent>) => {
        if (MapBrowserEventType.POINTERMOVE === event.type) {
          const features: FeatureLike[] = event.map.getFeaturesAtPixel(event.pixel, {
            hitTolerance: 10,
          });
          const relationIds: string[] = [];
          if (features && features.length > 0) {
            features.forEach((feature) => {
              const layer = feature.get('layer');
              if (layer === 'relation') {
                const id = feature.get('id');
                relationIds.push(id);
              }
            });
          }
          event.map.getTargetElement().style.cursor =
            relationIds.length >= 1 ? 'pointer' : 'default';
          if (relationIds.length >= 1) {
            const uniqueRelationIds: number[] = [];
            relationIds
              .map((r) => +r)
              .forEach((relationId) => {
                if (!uniqueRelationIds.includes(relationId)) {
                  uniqueRelationIds.push(relationId);
                }
              });
            uniqueRelationIds.sort((a, b) => a - b);
            const relationIdString = JSON.stringify(uniqueRelationIds);
            if (this.oldRrelationIdString !== relationIdString) {
              this.oldRrelationIdString = relationIdString;
              console.log('relationIds=' + relationIdString);
            }
          }
          return true; // propagate event
        }
        return true; // propagate event
      },
    });
  }
}
