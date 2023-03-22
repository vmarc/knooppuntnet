import { TrackPath } from '@api/common/common/track-path';
import { TrackPoint } from '@api/common/common/track-point';
import { TrackSegment } from '@api/common/common/track-segment';
import { RouteMap } from '@api/common/route/route-map';
import { RouteNetworkNodeInfo } from '@api/common/route/route-network-node-info';
import { I18nService } from '@app/i18n/i18n.service';
import { List } from 'immutable';
import { Color } from 'ol/color';
import Feature from 'ol/Feature';
import { Geometry } from 'ol/geom';
import { Point } from 'ol/geom';
import LineString from 'ol/geom/LineString';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';
import { Util } from '../../shared/util';
import { Marker } from '../domain/marker';
import { Layers } from './layers';
import { MapLayer } from './map-layer';

export class RouteLayers {
  constructor(private i18nService: I18nService, private routeMap: RouteMap) {}

  build(): List<MapLayer> {
    let layers: MapLayer[] = [];
    layers.push(this.buildMarkerLayer());
    layers = layers.concat(this.buildFreePathsLayers());
    layers.push(this.buildForwardLayer());
    layers.push(this.buildBackwardLayer());
    layers.push(this.buildStartTentaclesLayer());
    layers.push(this.buildEndTentaclesLayer());
    layers.push(this.buildUnusedSegmentsLayer());

    return List(layers).filter((layer) => layer !== null);
  }

  private buildFreePathsLayers(): MapLayer[] {
    return this.routeMap.freePaths.map((path) => {
      const translatedTitle = this.i18nService.translation(
        '@@map.layer.free-path'
      );
      const title = `${translatedTitle} ${path.pathId}`;
      const source = new VectorSource();
      const layer = new VectorLayer({ source });
      source.addFeature(this.pathToFeature(title, [0, 0, 255, 0.3], path));
      layer.set('name', title);
      const name = `path-layer-${path.pathId}`;
      return MapLayer.simpleLayer(name, layer);
    });
  }

  private buildForwardLayer(): MapLayer {
    const path = this.routeMap.forwardPath;
    if (path && path.segments.length > 0) {
      const title = this.i18nService.translation('@@map.layer.forward-route');
      const source = new VectorSource();
      const layer = new VectorLayer({
        source,
      });
      source.addFeature(this.pathToFeature(title, [0, 0, 255, 0.3], path));
      layer.set('name', title);
      return MapLayer.simpleLayer('route-forward-layer', layer);
    }
    return null;
  }

  private buildBackwardLayer(): MapLayer {
    const path = this.routeMap.backwardPath;
    if (path) {
      const title = this.i18nService.translation('@@map.layer.backward-route');
      const source = new VectorSource();
      const layer = new VectorLayer({
        source,
      });
      source.addFeature(this.pathToFeature(title, [0, 0, 255, 0.3], path));
      layer.set('name', title);
      return MapLayer.simpleLayer('route-backward-layer', layer);
    }
    return null;
  }

  private buildStartTentaclesLayer(): MapLayer {
    const paths = this.routeMap.startTentaclePaths;
    if (paths && paths.length > 0) {
      const title = this.i18nService.translation('@@map.layer.start-tentacle');
      const source = new VectorSource();
      const layer = new VectorLayer({
        source,
      });
      paths.forEach((path) => {
        source.addFeature(this.pathToFeature(title, [0, 0, 255, 0.3], path));
      });
      layer.set('name', title);
      return MapLayer.simpleLayer('route-start-tentacle-layer', layer);
    }
    return null;
  }

  private buildEndTentaclesLayer(): MapLayer {
    const paths = this.routeMap.endTentaclePaths;
    if (paths && paths.length > 0) {
      const title = this.i18nService.translation('@@map.layer.end-tentacle');
      const source = new VectorSource();
      const layer = new VectorLayer({
        source,
      });
      paths.forEach((path) => {
        source.addFeature(this.pathToFeature(title, [0, 0, 255, 0.3], path));
      });
      layer.set('name', title);
      return MapLayer.simpleLayer('route-end-tentacles-layer', layer);
    }
    return null;
  }

  private buildUnusedSegmentsLayer(): MapLayer {
    const segments = this.routeMap.unusedSegments;
    if (segments && segments.length > 0) {
      const title = this.i18nService.translation('@@map.layer.unused');
      const source = new VectorSource();
      const layer = new VectorLayer({
        source,
      });
      segments.forEach((segment) => {
        source.addFeature(
          this.segmentToFeature(title, [255, 0, 0, 0.3], segment)
        );
      });
      layer.set('name', title);
      return MapLayer.simpleLayer('route-unused-segment-layer', layer);
    }
    return null;
  }

  private buildMarkerLayer(): MapLayer {
    const freeNodeMarkers = this.buildMarkers(
      this.routeMap.freeNodes,
      'blue',
      '@@map.free-node'
    );
    const startNodeMarkers = this.buildMarkers(
      this.routeMap.startNodes,
      'green',
      '@@map.start-node'
    );
    const endNodeMarkers = this.buildMarkers(
      this.routeMap.endNodes,
      'red',
      '@@map.end-node'
    );
    const startTentacleNodeMarkers = this.buildMarkers(
      this.routeMap.startTentacleNodes,
      'orange',
      '@@map.start-tentacle-node'
    );
    const endTentacleNodeMarkers = this.buildMarkers(
      this.routeMap.endTentacleNodes,
      'purple',
      '@@map.end-tentacle-node'
    );
    const redundantNodeMarkers = this.buildMarkers(
      this.routeMap.redundantNodes,
      'yellow',
      '@@map.redundant-node'
    );
    const markers: Feature<Point>[] = freeNodeMarkers
      .concat(startNodeMarkers)
      .concat(endNodeMarkers)
      .concat(startTentacleNodeMarkers)
      .concat(endTentacleNodeMarkers)
      .concat(redundantNodeMarkers);

    const source = new VectorSource();
    const layer = new VectorLayer({
      zIndex: Layers.zIndexNetworkNodesLayer,
      className: 'route-marker',
      source,
    });

    source.addFeatures(markers);
    const layerName = this.i18nService.translation('@@map.layer.nodes');
    layer.set('name', layerName);
    return MapLayer.simpleLayer('route-marker-layer', layer);
  }

  private buildMarkers(
    nodes: RouteNetworkNodeInfo[],
    color: string,
    nodeType: string
  ): Feature<Point>[] {
    const translatedNodeType = this.i18nService.translation(nodeType);
    return nodes.map((node) => {
      const coordinate = Util.toCoordinate(node.lat, node.lon);
      const marker = Marker.create(color, coordinate);
      marker.set('name', translatedNodeType);
      return marker;
    });
  }

  private pathToFeature(
    title: string,
    color: Color,
    path: TrackPath
  ): Feature<Geometry> {
    const trackPointArray: Array<TrackPoint> = [];
    trackPointArray.push(path.segments[0].source);
    path.segments.forEach((segment) => {
      segment.fragments.forEach((fragment) =>
        trackPointArray.push(fragment.trackPoint)
      );
    });
    const trackPoints = List(trackPointArray);
    return this.trackPointsToFeature(title, color, trackPoints);
  }

  private segmentToFeature(
    title: string,
    color: Color,
    segment: TrackSegment
  ): Feature<Geometry> {
    let trackPoints = List<TrackPoint>([segment.source]);
    trackPoints = trackPoints.concat(
      segment.fragments.map((fragment) => fragment.trackPoint)
    );
    return this.trackPointsToFeature(title, color, trackPoints);
  }

  private trackPointsToFeature(
    title: string,
    color: Color,
    trackPoints: List<TrackPoint>
  ): Feature<Geometry> {
    const coordinates = trackPoints.map((trackPoint) =>
      Util.toCoordinate(trackPoint.lat, trackPoint.lon)
    );
    const polygon = new LineString(coordinates.toArray());
    const feature = new Feature(polygon);
    const style = new Style({
      stroke: new Stroke({
        color,
        width: 15,
      }),
    });
    feature.setStyle(style);
    return feature;
  }
}
