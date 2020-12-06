import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import {GeoJSON} from 'ol/format';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import {Stroke} from 'ol/style';
import {Style} from 'ol/style';
import {AppState} from '../../core/core.state';
import {selectLongDistanceRouteMapGpxOkVisible} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteMapGpxNokVisible} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteMapOsmRelationVisible} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteMapGpxVisible} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteMap} from '../../core/longdistance/long-distance.selectors';
import {Subscriptions} from '../../util/Subscriptions';

@Injectable({
  providedIn: 'root'
})
export class LongDistanceRouteMapService {

  private readonly response$ = this.store.select(selectLongDistanceRouteMap);

  private readonly gpxLayer: VectorLayer;
  private readonly gpxOkLayer: VectorLayer;
  private readonly gpxNokLayer: VectorLayer;
  private readonly osmRelationLayer: VectorLayer;

  private readonly subscriptions = new Subscriptions();

  constructor(private store: Store<AppState>) {
    this.gpxLayer = this.buildGpxLayer();
    this.gpxOkLayer = this.buildGpxOkLayer();
    this.gpxNokLayer = this.buildGpxNokLayer();
    this.osmRelationLayer = this.buildOsmRelationLayer();
    this.initialize();
  }

  layers(): VectorLayer[] {
    return [
      this.gpxLayer,
      this.gpxOkLayer,
      this.gpxNokLayer,
      this.osmRelationLayer
    ];
  }

  finalization(): void {
    this.subscriptions.unsubscribe();
  }

  private initialize(): void {

    this.subscriptions.add(
      this.store.select(selectLongDistanceRouteMapGpxVisible).subscribe(visible => {
        this.gpxLayer.setVisible(visible);
      })
    );

    this.subscriptions.add(
      this.store.select(selectLongDistanceRouteMapGpxOkVisible).subscribe(visible => {
        this.gpxOkLayer.setVisible(visible);
      })
    );

    this.subscriptions.add(
      this.store.select(selectLongDistanceRouteMapGpxNokVisible).subscribe(visible => {
        this.gpxNokLayer.setVisible(visible);
      })
    );

    this.subscriptions.add(
      this.store.select(selectLongDistanceRouteMapOsmRelationVisible).subscribe(visible => {
        this.osmRelationLayer.setVisible(visible);
      })
    );

    this.subscriptions.add(
      this.response$.subscribe(response => {
        if (response?.result?.gpxGeometry) {
          const features = new GeoJSON().readFeatures(response.result.gpxGeometry, {featureProjection: 'EPSG:3857'});
          this.gpxLayer.setSource(new VectorSource({features}));
        } else {
          this.gpxLayer.setSource(new VectorSource());
        }

        if (response?.result?.okGeometry) {
          const features = new GeoJSON().readFeatures(response.result.okGeometry, {featureProjection: 'EPSG:3857'});
          this.gpxOkLayer.setSource(new VectorSource({features}));
        } else {
          this.gpxOkLayer.setSource(new VectorSource());
        }

        if (response?.result?.nokSegments) {
          let features = [];
          response.result.nokSegments.forEach(segment => {
            new GeoJSON().readFeatures(segment.geoJson, {featureProjection: 'EPSG:3857'}).forEach(feature => features.push(feature));
          });
          this.gpxNokLayer.setSource(new VectorSource({features}));
        } else {
          this.gpxNokLayer.setSource(new VectorSource());
        }

        if (response?.result?.osmSegments) {
          let features = [];
          response.result.osmSegments.forEach(segment => {
            new GeoJSON().readFeatures(segment.geoJson, {featureProjection: 'EPSG:3857'}).forEach(feature => features.push(feature));
          });
          this.osmRelationLayer.setSource(new VectorSource({features}));
        } else {
          this.osmRelationLayer.setSource(new VectorSource());
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
      style: styleFunction
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
      style: styleFunction
    });
  }

  private buildOsmRelationLayer(): VectorLayer {

    const layerStyle = this.fixedStyle('yellow', 10);

    const styleFunction = function (feature) {
      return layerStyle;
    };

    return new VectorLayer({
      zIndex: 40,
      source: new VectorSource(),
      style: styleFunction
    });
  }

  private fixedStyle(color: string, width: number): Style {
    return new Style({
      stroke: new Stroke({
        color: color,
        width: width
      })
    });
  }

}
