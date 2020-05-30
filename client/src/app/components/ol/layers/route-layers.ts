import {List} from "immutable";
import {Color} from "ol/color";
import Feature from "ol/Feature";
import LineString from "ol/geom/LineString";
import {Layer} from "ol/layer";
import BaseLayer from "ol/layer/Base";
import VectorLayer from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import Stroke from "ol/style/Stroke";
import Style from "ol/style/Style";
import {I18nService} from "../../../i18n/i18n.service";
import {TrackPath} from "../../../kpn/api/common/common/track-path";
import {TrackPoint} from "../../../kpn/api/common/common/track-point";
import {TrackSegment} from "../../../kpn/api/common/common/track-segment";
import {RouteMap} from "../../../kpn/api/common/route/route-map";
import {RouteNetworkNodeInfo} from "../../../kpn/api/common/route/route-network-node-info";
import {Util} from "../../shared/util";
import {Marker} from "../domain/marker";
import {MapLayer} from "./map-layer";

export class RouteLayers {

  constructor(private i18nService: I18nService, private routeMap: RouteMap) {
  }

  build(): List<MapLayer> {
    return List([
      this.buildMarkerLayer(),
      this.buildForwardLayer(),
      this.buildBackwardLayer(),
      this.buildStartTentaclesLayer(),
      this.buildEndTentaclesLayer(),
      this.buildUnusedSegmentsLayer()
    ]).filter(layer => layer !== null);
  }

  private buildForwardLayer(): MapLayer {
    const path = this.routeMap.forwardPath;
    if (path && !path.segments.isEmpty()) {
      const title = this.i18nService.translation("@@map.layer.forward-route");
      const source = new VectorSource();
      const layer = new VectorLayer({
        source: source
      });
      source.addFeature(this.pathToFeature(title, [0, 0, 255, 0.3], path));
      layer.set("name", title);
      return new MapLayer("route-forward-layer", layer);
    }
    return null;
  }

  private buildBackwardLayer(): MapLayer {
    const path = this.routeMap.backwardPath;
    if (path) {
      const title = this.i18nService.translation("@@map.layer.backward-route");
      const source = new VectorSource();
      const layer = new VectorLayer({
        source: source
      });
      source.addFeature(this.pathToFeature(title, [0, 0, 255, 0.3], path));
      layer.set("name", title);
      return new MapLayer("route-backward-layer", layer);
    }
    return null;
  }

  private buildStartTentaclesLayer(): MapLayer {
    const paths = this.routeMap.startTentaclePaths;
    if (paths && !paths.isEmpty()) {
      const title = this.i18nService.translation("@@map.layer.start-tentacle");
      const source = new VectorSource();
      const layer = new VectorLayer({
        source: source
      });
      paths.forEach(path => {
        source.addFeature(this.pathToFeature(title, [0, 0, 255, 0.3], path));
      });
      layer.set("name", title);
      return new MapLayer("route-start-tentacle-layer", layer);
    }
    return null;
  }

  private buildEndTentaclesLayer(): MapLayer {
    const paths = this.routeMap.endTentaclePaths;
    if (paths && !paths.isEmpty()) {
      const title = this.i18nService.translation("@@map.layer.end-tentacle");
      const source = new VectorSource();
      const layer = new VectorLayer({
        source: source
      });
      paths.forEach(path => {
        source.addFeature(this.pathToFeature(title, [0, 0, 255, 0.3], path));
      });
      layer.set("name", title);
      return new MapLayer("route-end-tentacles-layer", layer);
    }
    return null;
  }

  private buildUnusedSegmentsLayer(): MapLayer {
    const segments = this.routeMap.unusedSegments;
    if (segments && !segments.isEmpty()) {
      const title = this.i18nService.translation("@@map.layer.unused");
      const source = new VectorSource();
      const layer = new VectorLayer({
        source: source
      });
      segments.forEach(segment => {
        source.addFeature(this.segmentToFeature(title, [255, 0, 0, 0.3], segment));
      });
      layer.set("name", title);
      return new MapLayer("route-unused-segment-layer", layer);
    }
    return null;
  }

  private buildMarkerLayer(): MapLayer {
    const startNodeMarkers = this.buildMarkers(this.routeMap.startNodes, "green", "@@map.start-node");
    const endNodeMarkers = this.buildMarkers(this.routeMap.endNodes, "red", "@@map.end-node");
    const startTentacleNodeMarkers = this.buildMarkers(this.routeMap.startTentacleNodes, "orange", "@@map.start-tentacle-node");
    const endTentacleNodeMarkers = this.buildMarkers(this.routeMap.endTentacleNodes, "purple", "@@map.end-tentacle-node");
    const redundantNodeMarkers = this.buildMarkers(this.routeMap.redundantNodes, "yellow", "@@map.redundant-node");
    const markers: List<Feature> = startNodeMarkers.concat(endNodeMarkers)
      .concat(startTentacleNodeMarkers)
      .concat(endTentacleNodeMarkers)
      .concat(redundantNodeMarkers);

    const source = new VectorSource();
    const layer = new VectorLayer({
      source: source
    });

    source.addFeatures(markers.toArray());
    const layerName = this.i18nService.translation("@@map.layer.nodes");
    layer.set("name", layerName);
    return new MapLayer("route-marker-layer", layer);
  }

  private buildMarkers(nodes: List<RouteNetworkNodeInfo>, color: string, nodeType: string): List<Feature> {
    const translatedNodeType = this.i18nService.translation(nodeType);
    return nodes.map(node => {
      const coordinate = Util.toCoordinate(node.lat, node.lon);
      const marker = Marker.create(color, coordinate);
      marker.set("name", translatedNodeType);
      return marker;
    });
  }

  private pathToFeature(title: string, color: Color, path: TrackPath): Feature {
    const trackPoints = List([path.segments.get(0).source]).concat(
      path.segments.flatMap(segment => segment.fragments.map(fragment => fragment.trackPoint))
    );
    return this.trackPointsToFeature(title, color, trackPoints);
  }

  private segmentToFeature(title: string, color: Color, segment: TrackSegment): Feature {
    let trackPoints = List<TrackPoint>([segment.source]);
    trackPoints = trackPoints.concat(segment.fragments.map(fragment => fragment.trackPoint));
    return this.trackPointsToFeature(title, color, trackPoints);
  }

  private trackPointsToFeature(title: string, color: Color, trackPoints: List<TrackPoint>): Feature {
    const coordinates = trackPoints.map(trackPoint => Util.toCoordinate(trackPoint.lat, trackPoint.lon));
    const polygon = new LineString(coordinates.toArray());
    const feature = new Feature(polygon);
    const style = new Style({
      stroke: new Stroke({
        color: color,
        width: 15
      })
    });
    feature.setStyle(style);
    return feature;
  }

}
