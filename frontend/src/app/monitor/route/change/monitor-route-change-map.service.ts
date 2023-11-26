import { Injectable } from '@angular/core';
import { MonitorRouteDeviation } from '@api/common/monitor';
import { MonitorRouteSegment } from '@api/common/monitor';
import { ZoomLevel } from '@app/ol/domain';
import { BackgroundLayer } from '@app/ol/layers';
import { MapControls } from '@app/ol/layers';
import { MapLayer } from '@app/ol/layers';
import { MapLayerRegistry } from '@app/ol/layers';
import { OsmLayer } from '@app/ol/layers';
import { OpenlayersMapService } from '@app/ol/services';
import { Util } from '@app/components/shared';
import { GeoJSON } from 'ol/format';
import VectorLayer from 'ol/layer/Vector';
import Map from 'ol/Map';
import VectorSource from 'ol/source/Vector';
import { Stroke } from 'ol/style';
import { Style } from 'ol/style';
import View from 'ol/View';

@Injectable()
export class MonitorRouteChangeMapService extends OpenlayersMapService {
  constructor() {
    super();
  }

  init(
    referenceJson: string,
    deviation: MonitorRouteDeviation,
    routeSegments: MonitorRouteSegment[]
  ): void {
    this.registerLayers(referenceJson, deviation, routeSegments);
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
    this.map.getView().fit(Util.toExtent(deviation.bounds, 0.05));
    this.finalizeSetup();
  }

  private registerLayers(
    referenceJson: string,
    deviation: MonitorRouteDeviation,
    routeSegments: MonitorRouteSegment[]
  ): void {
    const registry = new MapLayerRegistry();
    registry.register([], BackgroundLayer.build(), true);
    registry.register([], OsmLayer.build(), false);
    registry.register([], this.buildReferenceLayer(referenceJson), true);
    registry.register([], this.buildNokSegmentLayer(deviation), true);
    registry.register([], this.buildOsmRelationLayer(routeSegments), true);
    this.register(registry);
  }

  private buildReferenceLayer(referenceJson: string): MapLayer {
    const layerStyle = this.fixedStyle('blue', 4);
    const features = new GeoJSON().readFeatures(referenceJson, {
      featureProjection: 'EPSG:3857',
    });
    const layer = new VectorLayer({
      zIndex: 50,
      source: new VectorSource({ features }),
      style: () => layerStyle,
    });

    layer.set('name', 'GPX reference'); // TODO planner: set elsewhere?
    return MapLayer.simpleLayer('gpx-reference-layer', layer);
  }

  private buildNokSegmentLayer(deviation: MonitorRouteDeviation): MapLayer {
    const layerStyle = this.fixedStyle('red', 4);
    const features = new GeoJSON().readFeatures(deviation.geoJson, {
      featureProjection: 'EPSG:3857',
    });

    const layer = new VectorLayer({
      zIndex: 70,
      source: new VectorSource({ features }),
      style: () => layerStyle,
    });
    layer.set('name', 'Not OK segment'); // TODO planner: set elsewhere?
    return MapLayer.simpleLayer('not-ok-layer', layer);
  }

  private buildOsmRelationLayer(
    routeSegments: MonitorRouteSegment[]
  ): MapLayer {
    const thickStyle = this.fixedStyle('yellow', 10);
    const features = [];
    routeSegments.forEach((segment) => {
      new GeoJSON()
        .readFeatures(segment.geoJson, { featureProjection: 'EPSG:3857' })
        .forEach((feature) => {
          feature.set('segmentId', segment.id);
          features.push(feature);
        });
    });

    const layer = new VectorLayer({
      zIndex: 40,
      source: new VectorSource({ features }),
      style: () => thickStyle,
    });
    layer.set('name', 'OSM Relation'); // TODO planner: set elsewhere?
    return MapLayer.simpleLayer('osm-relation-layer', layer);
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
