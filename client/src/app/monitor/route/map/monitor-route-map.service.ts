import { OnDestroy } from '@angular/core';
import { Injectable } from '@angular/core';
import { Bounds } from '@api/common/bounds';
import { Store } from '@ngrx/store';
import { GeoJSON } from 'ol/format';
import { Geometry } from 'ol/geom';
import VectorLayer from 'ol/layer/Vector';
import Map from 'ol/Map';
import VectorSource from 'ol/source/Vector';
import { Stroke } from 'ol/style';
import { Style } from 'ol/style';
import View from 'ol/View';
import { Util } from '../../../components/shared/util';
import { AppState } from '../../../core/core.state';
import { Subscriptions } from '../../../util/Subscriptions';
import { selectMonitorRouteMapReferenceEnabled } from '../../store/monitor.selectors';
import { selectMonitorRouteMapMode } from '../../store/monitor.selectors';
import { selectMonitorRouteMapMatchesVisible } from '../../store/monitor.selectors';
import { selectMonitorRouteMapDeviationsVisible } from '../../store/monitor.selectors';
import { selectMonitorRouteMapOsmRelationVisible } from '../../store/monitor.selectors';
import { selectMonitorRouteMapReferenceVisible } from '../../store/monitor.selectors';
import { selectMonitorRouteMapPage } from '../../store/monitor.selectors';

@Injectable({
  providedIn: 'root',
})
export class MonitorRouteMapService implements OnDestroy {
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

  private readonly response$ = this.store.select(selectMonitorRouteMapPage);

  private readonly referenceLayer: VectorLayer<VectorSource<Geometry>>;
  private readonly matchesLayer: VectorLayer<VectorSource<Geometry>>;
  private readonly deviationsLayer: VectorLayer<VectorSource<Geometry>>;
  private readonly osmRelationLayer: VectorLayer<VectorSource<Geometry>>;

  private readonly subscriptions = new Subscriptions();

  private mode = '';
  private referenceAvailable = false;

  private map: Map = null;

  constructor(private store: Store) {
    this.referenceLayer = this.buildReferencesLayer();
    this.matchesLayer = this.buildMatchesLayer();
    this.deviationsLayer = this.buildDeviationsLayer();
    this.osmRelationLayer = this.buildOsmRelationLayer();
    this.initialize();

    this.subscriptions.add(
      this.store.select(selectMonitorRouteMapMode).subscribe((mode) => {
        this.mode = mode;
        this.osmRelationLayer.changed();
      })
    );

    this.subscriptions.add(
      this.store
        .select(selectMonitorRouteMapReferenceEnabled)
        .subscribe((enabled) => {
          this.referenceAvailable = enabled;
        })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  setMap(map: Map): void {
    this.map = map;
  }

  focus(bounds: Bounds): void {
    if (this.map !== null && bounds) {
      this.map.getView().fit(Util.toExtent(bounds, 0.1));
    }
  }

  layers(): VectorLayer<VectorSource<Geometry>>[] {
    return [
      this.referenceLayer,
      this.matchesLayer,
      this.deviationsLayer,
      this.osmRelationLayer,
    ];
  }

  colorForSegmentId(id: number): string {
    const index = id % 10;
    return this.colors[index];
  }

  styleForSegmentId(id: number): Style {
    const index = id % 10;
    return this.osmSegmentStyles[index];
  }

  getView(): View {
    return this.map.getView();
  }

  private initialize(): void {
    this.subscriptions.add(
      this.store
        .select(selectMonitorRouteMapReferenceVisible)
        .subscribe((visible) => {
          this.referenceLayer.setVisible(visible);
        })
    );

    this.subscriptions.add(
      this.store
        .select(selectMonitorRouteMapMatchesVisible)
        .subscribe((visible) => {
          this.matchesLayer.setVisible(visible);
        })
    );

    this.subscriptions.add(
      this.store
        .select(selectMonitorRouteMapDeviationsVisible)
        .subscribe((visible) => {
          this.deviationsLayer.setVisible(visible);
        })
    );

    this.subscriptions.add(
      this.store
        .select(selectMonitorRouteMapOsmRelationVisible)
        .subscribe((visible) => {
          this.osmRelationLayer.setVisible(visible);
        })
    );

    this.subscriptions.add(
      this.response$.subscribe((response) => {
        this.referenceLayer.getSource().clear();
        if (response?.result?.reference?.geoJson) {
          const features = new GeoJSON().readFeatures(
            response.result.reference.geoJson,
            { featureProjection: 'EPSG:3857' }
          );
          this.referenceLayer.getSource().addFeatures(features);
        }

        this.matchesLayer.getSource().clear();
        if (response?.result?.matchesGeoJson) {
          const features = new GeoJSON().readFeatures(
            response.result.matchesGeoJson,
            { featureProjection: 'EPSG:3857' }
          );
          this.matchesLayer.getSource().addFeatures(features);
        }

        this.deviationsLayer.getSource().clear();
        if (response?.result?.deviations) {
          const features = [];
          response.result.deviations.forEach((segment) => {
            new GeoJSON()
              .readFeatures(segment.geoJson, { featureProjection: 'EPSG:3857' })
              .forEach((feature) => features.push(feature));
          });
          this.deviationsLayer.getSource().addFeatures(features);
        }

        this.osmRelationLayer.getSource().clear();
        if (response?.result?.osmSegments) {
          const features = [];
          response.result.osmSegments.forEach((segment) => {
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
