import { Injectable } from '@angular/core';
import { Bounds } from '@api/common';
import { MonitorRouteMapPage } from '@api/common/monitor';
import { MapPosition } from '@app/components/ol/domain';
import { ZoomLevel } from '@app/components/ol/domain';
import { BackgroundLayer } from '@app/components/ol/layers';
import { MapControls } from '@app/components/ol/layers';
import { MapLayerRegistry } from '@app/components/ol/layers';
import { OsmLayer } from '@app/components/ol/layers';
import { OpenlayersMapService } from '@app/components/ol/services';
import { Util } from '@app/components/shared';
import { Subscriptions } from '@app/util';
import { Store } from '@ngrx/store';
import { Coordinate } from 'ol/coordinate';
import { GeoJSON } from 'ol/format';
import { Geometry } from 'ol/geom';
import VectorLayer from 'ol/layer/Vector';
import Map from 'ol/Map';
import VectorSource from 'ol/source/Vector';
import { Stroke } from 'ol/style';
import { Style } from 'ol/style';
import View from 'ol/View';
import { selectMonitorRouteMapReferenceEnabled } from './store/monitor-route-map.selectors';
import { selectMonitorRouteMapMode } from './store/monitor-route-map.selectors';
import { selectMonitorRouteMapMatchesVisible } from './store/monitor-route-map.selectors';
import { selectMonitorRouteMapDeviationsVisible } from './store/monitor-route-map.selectors';
import { selectMonitorRouteMapOsmRelationVisible } from './store/monitor-route-map.selectors';
import { selectMonitorRouteMapReferenceVisible } from './store/monitor-route-map.selectors';
import { selectMonitorRouteMapPage } from './store/monitor-route-map.selectors';

@Injectable({
  providedIn: 'root',
})
export class MonitorRouteMapService extends OpenlayersMapService {
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

  private readonly page$ = this.store.select(selectMonitorRouteMapPage);

  private readonly referenceLayer: VectorLayer<VectorSource<Geometry>>;
  private readonly matchesLayer: VectorLayer<VectorSource<Geometry>>;
  private readonly deviationsLayer: VectorLayer<VectorSource<Geometry>>;
  private readonly osmRelationLayer: VectorLayer<VectorSource<Geometry>>;

  private readonly extraSubscriptions = new Subscriptions();

  private mode = '';
  private referenceAvailable = false;

  constructor(private store: Store) {
    super();
    this.referenceLayer = this.buildReferencesLayer();
    this.matchesLayer = this.buildMatchesLayer();
    this.deviationsLayer = this.buildDeviationsLayer();
    this.osmRelationLayer = this.buildOsmRelationLayer();
    this.initialize();

    this.extraSubscriptions.add(
      this.store.select(selectMonitorRouteMapMode).subscribe((mode) => {
        this.mode = mode;
        this.osmRelationLayer.changed();
      })
    );

    this.extraSubscriptions.add(
      this.store
        .select(selectMonitorRouteMapReferenceEnabled)
        .subscribe((enabled) => {
          this.referenceAvailable = enabled;
        })
    );
  }

  init(page: MonitorRouteMapPage, mapPositionFromUrl: MapPosition): void {
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
      this.map.getView().fit(Util.toExtent(page.bounds, 0.05));
    }
    this.finalizeSetup(true);
  }

  ngOnDestroy(): void {
    this.extraSubscriptions.unsubscribe();
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
    this.extraSubscriptions.add(
      this.store
        .select(selectMonitorRouteMapReferenceVisible)
        .subscribe((visible) => {
          this.referenceLayer.setVisible(visible);
        })
    );

    this.extraSubscriptions.add(
      this.store
        .select(selectMonitorRouteMapMatchesVisible)
        .subscribe((visible) => {
          this.matchesLayer.setVisible(visible);
        })
    );

    this.extraSubscriptions.add(
      this.store
        .select(selectMonitorRouteMapDeviationsVisible)
        .subscribe((visible) => {
          this.deviationsLayer.setVisible(visible);
        })
    );

    this.extraSubscriptions.add(
      this.store
        .select(selectMonitorRouteMapOsmRelationVisible)
        .subscribe((visible) => {
          this.osmRelationLayer.setVisible(visible);
        })
    );

    this.extraSubscriptions.add(
      this.page$.subscribe((page) => {
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
      })
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
    const self = this;
    const thinStyle = this.fixedStyle('gold', 4);
    const thickStyle = this.fixedStyle('gold', 10);

    const styleFunction = (feature) => {
      if (self.mode === 'osm-segments') {
        const segmentId = feature.get('segmentId');
        return self.styleForSegmentId(segmentId);
      }
      if (self.referenceAvailable) {
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
}
