import { Injectable } from '@angular/core';
import { BoundsI } from '@api/common/bounds-i';
import { Store } from '@ngrx/store';
import { GeoJSON } from 'ol/format';
import VectorLayer from 'ol/layer/Vector';
import Map from 'ol/Map';
import VectorSource from 'ol/source/Vector';
import { Stroke } from 'ol/style';
import { Style } from 'ol/style';
import { Util } from '../../../../components/shared/util';
import { AppState } from '../../../../core/core.state';
import { Subscriptions } from '../../../../util/Subscriptions';
import { selectLongdistanceRouteMapOsmRelationVisible } from '../../../store/monitor.selectors';
import { selectLongdistanceRouteMapGpxNokVisible } from '../../../store/monitor.selectors';
import { selectLongdistanceRouteMapGpxOkVisible } from '../../../store/monitor.selectors';
import { selectLongdistanceRouteMapGpxVisible } from '../../../store/monitor.selectors';
import { selectLongdistanceRouteMapGpxEnabled } from '../../../store/monitor.selectors';
import { selectLongdistanceRouteMapMode } from '../../../store/monitor.selectors';
import { selectLongdistanceRouteMapPage } from '../../../store/monitor.selectors';

@Injectable({
  providedIn: 'root',
})
export class LongdistanceRouteMapService {
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

  private readonly response$ = this.store.select(
    selectLongdistanceRouteMapPage
  );

  private readonly gpxLayer: VectorLayer;
  private readonly gpxOkLayer: VectorLayer;
  private readonly gpxNokLayer: VectorLayer;
  private readonly osmRelationLayer: VectorLayer;

  private readonly subscriptions = new Subscriptions();

  private mode = '';
  private gpxTraceAvailable = false;

  private map: Map = null;

  constructor(private store: Store<AppState>) {
    this.gpxLayer = this.buildGpxLayer();
    this.gpxOkLayer = this.buildGpxOkLayer();
    this.gpxNokLayer = this.buildGpxNokLayer();
    this.osmRelationLayer = this.buildOsmRelationLayer();
    this.initialize();

    this.subscriptions.add(
      this.store.select(selectLongdistanceRouteMapMode).subscribe((mode) => {
        this.mode = mode;
        this.osmRelationLayer.changed();
      })
    );

    this.subscriptions.add(
      this.store
        .select(selectLongdistanceRouteMapGpxEnabled)
        .subscribe((enabled) => {
          this.gpxTraceAvailable = enabled;
        })
    );
  }

  setMap(map: Map): void {
    this.map = map;
  }

  focus(bounds: BoundsI): void {
    if (this.map !== null) {
      this.map.getView().fit(Util.toExtent(bounds, 0.1));
    }
  }

  layers(): VectorLayer[] {
    return [
      this.gpxLayer,
      this.gpxOkLayer,
      this.gpxNokLayer,
      this.osmRelationLayer,
    ];
  }

  finalization(): void {
    this.subscriptions.unsubscribe();
  }

  colorForSegmentId(id: number): string {
    const index = id % 10;
    return this.colors[index];
  }

  styleForSegmentId(id: number): Style {
    const index = id % 10;
    return this.osmSegmentStyles[index];
  }

  private initialize(): void {
    this.subscriptions.add(
      this.store
        .select(selectLongdistanceRouteMapGpxVisible)
        .subscribe((visible) => {
          this.gpxLayer.setVisible(visible);
        })
    );

    this.subscriptions.add(
      this.store
        .select(selectLongdistanceRouteMapGpxOkVisible)
        .subscribe((visible) => {
          this.gpxOkLayer.setVisible(visible);
        })
    );

    this.subscriptions.add(
      this.store
        .select(selectLongdistanceRouteMapGpxNokVisible)
        .subscribe((visible) => {
          this.gpxNokLayer.setVisible(visible);
        })
    );

    this.subscriptions.add(
      this.store
        .select(selectLongdistanceRouteMapOsmRelationVisible)
        .subscribe((visible) => {
          this.osmRelationLayer.setVisible(visible);
        })
    );

    this.subscriptions.add(
      this.response$.subscribe((response) => {
        this.gpxLayer.getSource().clear();
        if (response?.result?.gpxGeometry) {
          const features = new GeoJSON().readFeatures(
            response.result.gpxGeometry,
            { featureProjection: 'EPSG:3857' }
          );
          this.gpxLayer.getSource().addFeatures(features);
        }

        this.gpxOkLayer.getSource().clear();
        if (response?.result?.okGeometry) {
          const features = new GeoJSON().readFeatures(
            response.result.okGeometry,
            { featureProjection: 'EPSG:3857' }
          );
          this.gpxOkLayer.getSource().addFeatures(features);
        }

        this.gpxNokLayer.getSource().clear();
        if (response?.result?.nokSegments) {
          const features = [];
          response.result.nokSegments.forEach((segment) => {
            new GeoJSON()
              .readFeatures(segment.geoJson, { featureProjection: 'EPSG:3857' })
              .forEach((feature) => features.push(feature));
          });
          this.gpxNokLayer.getSource().addFeatures(features);
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

  private buildGpxLayer(): VectorLayer {
    const layerStyle = this.fixedStyle('blue', 4);
    const styleFunction = function (feature) {
      return layerStyle;
    };

    return new VectorLayer({
      zIndex: 50,
      source: new VectorSource(),
      style: styleFunction,
    });
  }

  private buildGpxOkLayer(): VectorLayer {
    const layerStyle = this.fixedStyle('green', 4);

    const styleFunction = function (feature) {
      return layerStyle;
    };

    return new VectorLayer({
      zIndex: 60,
      source: new VectorSource(),
      style: styleFunction,
    });
  }

  private buildGpxNokLayer(): VectorLayer {
    const layerStyle = this.fixedStyle('red', 4);

    const styleFunction = function (feature) {
      return layerStyle;
    };

    return new VectorLayer({
      zIndex: 70,
      source: new VectorSource(),
      style: styleFunction,
    });
  }

  private buildOsmRelationLayer(): VectorLayer {
    const self = this;
    const thinStyle = this.fixedStyle('yellow', 4);
    const thickStyle = this.fixedStyle('yellow', 10);
    const styleFunction = function (feature) {
      if (self.mode === 'osm-segments') {
        const segmentId = feature.get('segmentId');
        return self.styleForSegmentId(segmentId);
      }
      if (self.gpxTraceAvailable) {
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
