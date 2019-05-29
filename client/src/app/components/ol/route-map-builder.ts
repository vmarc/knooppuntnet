import {List} from "immutable";
import Color from "ol";
import Feature from "ol/Feature";
import LineString from "ol/geom/LineString";
import Layer from "ol/layer";
import VectorLayer from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import Stroke from "ol/style/Stroke";
import Style from "ol/style/Style";
import Extent from "ol/View";
import {TrackPath} from "../../kpn/shared/common/track-path";
import {TrackPoint} from "../../kpn/shared/common/track-point";
import {TrackSegment} from "../../kpn/shared/common/track-segment";
import {RouteInfo} from "../../kpn/shared/route/route-info";
import {RouteNetworkNodeInfo} from "../../kpn/shared/route/route-network-node-info";
import {Util} from "../shared/util";
import {DebugLayer} from "./domain/debug-layer";
import {Marker} from "./domain/marker";
import {NetworkBitmapTileLayer} from "./domain/network-bitmap-tile-layer";
import {NetworkVectorTileLayer} from "./domain/network-vector-tile-layer";
import {OsmLayer} from "./domain/osm-layer";

export class RouteMapBuilder {

  constructor(private routeInfo: RouteInfo) {
  }

  buildMap() {
  }

  private buildLayers() {
    /*
      override val layers: Seq[ol.layer.Base] = Seq(
        Some(Layers.osm),
        Some(vectorTileLayer.layer),
        Some(buildMarkerLayer()),
        forwardLayer(),
        backwardLayer(),
        startTentacles(),
        endTentacles(),
        unusedSegments()
      ).flatten
    */

    return [
      OsmLayer.build(),
      this.buildBitmapTileLayer(),
      this.buildVectorTileLayer(),
      DebugLayer.build()
    ];


  }

  buildExtent(): Extent {
    const bounds = this.routeInfo.analysis.map.bounds;
    const min = Util.toCoordinate(bounds.latMin, bounds.lonMin);
    const max = Util.toCoordinate(bounds.latMax, bounds.lonMax);
    return new Extent(min, max);
  }

  private buildBitmapTileLayer(): Layer {
    return NetworkBitmapTileLayer.build(this.routeInfo.summary.networkType);
  }

  private buildVectorTileLayer(): Layer {
    return NetworkVectorTileLayer.build(this.routeInfo.summary.networkType);
  }

  private buildForwardLayer(): Layer {
    const path = this.routeInfo.analysis.map.forwardPath;
    if (path) {
      const title = "Forward route"; // TODO translate: "Heen weg"
      const source = new VectorSource();
      const layer = new VectorLayer({
        source: source
      });
      source.addFeature(this.pathToFeature(title, new Color(0, 0, 255, 0.3), path));
      layer.set("name", title);
      return layer;
    }
    return null;
  }

  private buildBackwardLayer(): Layer {
    const path = this.routeInfo.analysis.map.backwardPath;
    if (path) {
      const title = "Backward route"; // TODO translate: "Terug weg"
      const source = new VectorSource();
      const layer = new VectorLayer({
        source: source
      });
      source.addFeature(this.pathToFeature(title, new Color(0, 0, 255, 0.3), path));
      layer.set("name", title);
      return layer;
    }
    return null;
  }

  private buildStartTentacles(): Layer {
    const paths = this.routeInfo.analysis.map.startTentaclePaths;
    if (paths && !paths.isEmpty()) {
      const title = "Start tentacle"; // TODO translate: "Start tentakel"
      const source = new VectorSource();
      const layer = new VectorLayer({
        source: source
      });
      paths.forEach(path => {
        source.addFeature(this.pathToFeature(title, new Color(0, 0, 255, 0.3), path));
      });
      layer.set("name", title);
      return layer;
    }
    return null;
  }

  private buildEndTentacles(): Layer {
    const paths = this.routeInfo.analysis.map.endTentaclePaths;
    if (paths && !paths.isEmpty()) {
      const title = "End tentacle"; // TODO translate: "Eind tentakel"
      const source = new VectorSource();
      const layer = new VectorLayer({
        source: source
      });
      paths.forEach(path => {
        source.addFeature(this.pathToFeature(title, new Color(0, 0, 255, 0.3), path));
      });
      layer.set("name", title);
      return layer;
    }
    return null;
  }

  private buildUnusedSegmentsLayer(): Layer {
    const segments = this.routeInfo.analysis.map.unusedSegments;
    if (segments && !segments.isEmpty()) {
      const title = "Unused"; // TODO translate: "Ongebruikt"
      const source = new VectorSource();
      const layer = new VectorLayer({
        source: source
      });
      segments.forEach(segment => {
        source.addFeature(this.segmentToFeature(title, new Color(255, 0, 0, 0.3), segment));
      });
      layer.set("name", title);
      return layer;
    }
    return null;
  }

  private buildMarkerLayer(): Layer {
    const routeMap = this.routeInfo.analysis.map;
    const startNodeMarkers = this.buildMarkers(routeMap.startNodes, "green", "Start node"); // TODO translate "Start knooppunt"
    const endNodeMarkers = this.buildMarkers(routeMap.endNodes, "red", "End node"); // TODO translate "Eind knooppunt"
    const startTentacleNodeMarkers = this.buildMarkers(routeMap.startTentacleNodes, "orange", "Start tentacle node"); // TODO translate "Start tentakel knooppunt"
    const endTentacleNodeMarkers = this.buildMarkers(routeMap.endTentacleNodes, "purple", "End tentacle node"); // TODO translate "Eind tentakel knooppunt"
    const redundantNodeMarkers = this.buildMarkers(routeMap.redundantNodes, "yellow", "Redundant node"); // TODO translate "Bijkomend knooppunt"
    const markers = startNodeMarkers.concat(endNodeMarkers).concat(startTentacleNodeMarkers).concat(endTentacleNodeMarkers).concat(redundantNodeMarkers);


    const source = new VectorSource();
    const layer = new VectorLayer({
      source: source
    });
    source.addFeatures(markers);
    layer.set("name", "Nodes"); // TODO translate: "Knooppunten"
    return layer;
  }

  private buildMarkers(nodes: List<RouteNetworkNodeInfo>, color: String, nodeType: String): List<Feature> {
    return nodes.map(node => {
      const coordinate = Util.toCoordinate(node.lat, node.lon);
      return Marker.create(coordinate, color);
    });
  }

  private pathToFeature(title: string, color: Color, path: TrackPath): Feature {
    if (!path.segments.isEmpty()) {
      const trackPoints = List([path.segments.get(0).source]).concat(
        path.segments.flatMap(segment => segment.fragments.map(fragment => fragment.trackPoint))
      );
      this.trackPointsToFeature(title, color, trackPoints);
    }
  }

  private segmentToFeature(title: string, color: Color, segment: TrackSegment): Feature {
    let trackPoints = List<TrackPoint>([segment.source]);
    trackPoints = trackPoints.concat(segment.fragments.map(fragment => fragment.trackPoint));
    return this.trackPointsToFeature(title, color, trackPoints)
  }

  private trackPointsToFeature(title: string, color: Color, trackPoints: List<TrackPoint>): Feature {
    const coordinates = trackPoints.map(trackPoint => Util.toCoordinate(trackPoint.lat, trackPoint.lon));
    const polygon = new LineString(coordinates.toArray());
    const feature = new Feature(polygon);
    const style = new Style({
      stroke: new Stroke({
        color: color,
        lineDash: [25, 25],
        width: 15
      })
    });
    feature.setStyle(style);
    return feature;
  }

}
