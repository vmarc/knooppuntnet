import {AfterViewInit, Component, Input, OnInit} from "@angular/core";
import {List} from "immutable";
import Color from "ol/color";
import {Attribution, defaults as defaultControls} from "ol/control";
import Feature from "ol/Feature";
import LineString from "ol/geom/LineString";
import Layer from "ol/layer";
import TileLayer from "ol/layer/Tile";
import VectorLayer from "ol/layer/Vector";
import VectorTileLayer from "ol/layer/VectorTile";
import Map from "ol/Map";
import VectorSource from "ol/source/Vector";
import Stroke from "ol/style/Stroke";
import Style from "ol/style/Style";
import View from "ol/View";
import Extent from "ol/View";
import {I18nService} from "../../i18n/i18n.service";
import {TrackPath} from "../../kpn/api/common/common/track-path";
import {TrackPoint} from "../../kpn/api/common/common/track-point";
import {TrackSegment} from "../../kpn/api/common/common/track-segment";
import {RouteInfo} from "../../kpn/api/common/route/route-info";
import {RouteNetworkNodeInfo} from "../../kpn/api/common/route/route-network-node-info";
import {Util} from "../shared/util";
import {MainMapStyle} from "./domain/main-map-style";
import {Marker} from "./domain/marker";
import {NetworkBitmapTileLayer} from "./domain/network-bitmap-tile-layer";
import {NetworkVectorTileLayer} from "./domain/network-vector-tile-layer";
import {NodeMapStyle} from "./domain/node-map-style";
import {OsmLayer} from "./domain/osm-layer";
import {ZoomLevel} from "./domain/zoom-level";
import {MapClickService} from "./map-click.service";
import {MapService} from "./map.service";

@Component({
  selector: "kpn-route-map",
  template: `
    <div id="route-map" class="map">
      <kpn-layer-switcher [layers]="layers"></kpn-layer-switcher>
    </div>
  `,
  styles: [`
    .map {
      position: absolute;
      top: 170px;
      left: 0;
      right: 0;
      bottom: 0;
    }
  `]
})
export class RouteMapComponent implements OnInit, AfterViewInit {

  @Input() routeInfo: RouteInfo;

  layers: List<Layer> = List();

  private map: Map;
  private bitmapTileLayer: TileLayer;
  private vectorTileLayer: VectorTileLayer;

  constructor(private mapClickService: MapClickService,
              private mapService: MapService,
              private i18nService: I18nService) {
  }

  ngOnInit(): void {
    this.layers = this.buildLayers();
  }

  ngAfterViewInit(): void {

    const attribution = new Attribution({
      collapsible: false
    });

    this.map = new Map({
      declutter: true,
      target: "route-map",
      layers: this.layers.toArray(),
      controls: defaultControls({attribution: false}).extend([attribution]),
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.maxZoom
      })
    });

    this.mapClickService.installOn(this.map);

    const view = this.map.getView();
    view.fit(this.buildExtent());
    view.on("change:resolution", () => this.zoom(view.getZoom()));

    const nodeMapStyle = new NodeMapStyle(this.map).styleFunction();
    this.vectorTileLayer.setStyle(nodeMapStyle);
    this.updateLayerVisibility(view.getZoom());
  }

  updateSize() {
    if (this.map != null) {
      this.map.updateSize();
    }
  }

  private buildLayers(): List<Layer> {

    this.bitmapTileLayer = this.buildBitmapTileLayer();
    this.vectorTileLayer = this.buildVectorTileLayer();

    const mainMapStyle = new MainMapStyle(this.map, this.mapService).styleFunction();
    this.vectorTileLayer.setStyle(mainMapStyle);

    return List([
      OsmLayer.build(),
      this.bitmapTileLayer,
      this.vectorTileLayer,
      this.buildMarkerLayer(),
      this.buildForwardLayer(),
      this.buildBackwardLayer(),
      this.buildStartTentaclesLayer(),
      this.buildEndTentaclesLayer(),
      this.buildUnusedSegmentsLayer()
    ]).filter(layer => layer !== null);
  }

  private buildExtent(): Extent {
    const bounds = this.routeInfo.analysis.map.bounds;
    const min = Util.toCoordinate(bounds.latMin, bounds.lonMin);
    const max = Util.toCoordinate(bounds.latMax, bounds.lonMax);
    return [min[0], min[1], max[0], max[1]];
  }

  private zoom(zoomLevel: number) {
    this.updateLayerVisibility(zoomLevel);
    return true;
  }

  private updateLayerVisibility(zoomLevel: number) {
    const zoom = Math.round(zoomLevel);
    if (zoom <= ZoomLevel.bitmapTileMaxZoom) {
      this.bitmapTileLayer.setVisible(true);
      this.vectorTileLayer.setVisible(false);
    } else if (zoom >= ZoomLevel.vectorTileMinZoom) {
      this.bitmapTileLayer.setVisible(false);
      this.vectorTileLayer.setVisible(true);
    }
  }

  private buildBitmapTileLayer(): Layer {
    return NetworkBitmapTileLayer.build(this.routeInfo.summary.networkType);
  }

  private buildVectorTileLayer(): Layer {
    return NetworkVectorTileLayer.build(this.routeInfo.summary.networkType);
  }

  private buildForwardLayer(): Layer {
    const path = this.routeInfo.analysis.map.forwardPath;
    if (path && !path.segments.isEmpty()) {
      const title = this.i18nService.translation("@@map.layer.forward-route");
      const source = new VectorSource();
      const layer = new VectorLayer({
        source: source
      });
      source.addFeature(this.pathToFeature(title, [0, 0, 255, 0.3], path));
      layer.set("name", title);
      return layer;
    }
    return null;
  }

  private buildBackwardLayer(): Layer {
    const path = this.routeInfo.analysis.map.backwardPath;
    if (path) {
      const title = this.i18nService.translation("@@map.layer.backward-route");
      const source = new VectorSource();
      const layer = new VectorLayer({
        source: source
      });
      source.addFeature(this.pathToFeature(title, [0, 0, 255, 0.3], path));
      layer.set("name", title);
      return layer;
    }
    return null;
  }

  private buildStartTentaclesLayer(): Layer {
    const paths = this.routeInfo.analysis.map.startTentaclePaths;
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
      return layer;
    }
    return null;
  }

  private buildEndTentaclesLayer(): Layer {
    const paths = this.routeInfo.analysis.map.endTentaclePaths;
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
      return layer;
    }
    return null;
  }

  private buildUnusedSegmentsLayer(): Layer {
    const segments = this.routeInfo.analysis.map.unusedSegments;
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
      return layer;
    }
    return null;
  }

  private buildMarkerLayer(): Layer {
    const routeMap = this.routeInfo.analysis.map;
    const startNodeMarkers = this.buildMarkers(routeMap.startNodes, "green", "@@map.start-node");
    const endNodeMarkers = this.buildMarkers(routeMap.endNodes, "red", "@@map.end-node");
    const startTentacleNodeMarkers = this.buildMarkers(routeMap.startTentacleNodes, "orange", "@@map.start-tentacle-node");
    const endTentacleNodeMarkers = this.buildMarkers(routeMap.endTentacleNodes, "purple", "@@map.end-tentacle-node");
    const redundantNodeMarkers = this.buildMarkers(routeMap.redundantNodes, "yellow", "@@map.redundant-node");
    const markers: List<Feature> = startNodeMarkers.concat(endNodeMarkers).concat(startTentacleNodeMarkers).concat(endTentacleNodeMarkers).concat(redundantNodeMarkers);

    const source = new VectorSource();
    const layer = new VectorLayer({
      source: source
    });

    source.addFeatures(markers.toArray());
    const layerName = this.i18nService.translation("@@map.layer.nodes");
    layer.set("name", layerName);
    return layer;
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
    return this.trackPointsToFeature(title, color, trackPoints)
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
