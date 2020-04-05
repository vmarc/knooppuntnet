import {AfterViewInit} from "@angular/core";
import {Input} from "@angular/core";
import {Component, OnInit} from "@angular/core";
import {List} from "immutable";
import {Color} from "ol/color";
import {defaults as defaultControls} from "ol/control";
import {Attribution} from "ol/control";
import {FullScreen} from "ol/control";
import {boundingExtent} from "ol/extent";
import Feature from "ol/Feature";
import LineString from "ol/geom/LineString";
import BaseLayer from "ol/layer/Base";
import VectorLayer from "ol/layer/Vector";
import Map from "ol/Map";
import {fromLonLat} from "ol/proj";
import VectorSource from "ol/source/Vector";
import {Stroke} from "ol/style";
import {Style} from "ol/style";
import View from "ol/View";
import {I18nService} from "../../i18n/i18n.service";
import {Bounds} from "../../kpn/api/common/bounds";
import {RawNode} from "../../kpn/api/common/data/raw/raw-node";
import {GeometryDiff} from "../../kpn/api/common/route/geometry-diff";
import {PointSegment} from "../../kpn/api/common/route/point-segment";
import {UniqueId} from "../../kpn/common/unique-id";
import {Util} from "../shared/util";
import {Marker} from "./domain/marker";
import {OsmLayer} from "./domain/osm-layer";
import {ZoomLevel} from "./domain/zoom-level";

@Component({
  selector: "kpn-route-change-map",
  template: `
    <div [id]="mapId" class="map">
      <kpn-layer-switcher [layers]="layers"></kpn-layer-switcher>
    </div>
  `,
  styles: [`
    .map {
      position: relative;
      left: 0;
      right: 20px;
      height: 320px;
      max-width: 640px;
      border: 1px solid lightgray;
      background-color: white;
    }
  `],
})
export class RouteChangeMapComponent implements OnInit, AfterViewInit {

  @Input() geometryDiff: GeometryDiff;
  @Input() nodes: List<RawNode>;
  @Input() bounds: Bounds;

  map: Map;
  mapId = UniqueId.get();
  layers: List<BaseLayer> = List();

  constructor(private i18nService: I18nService) {
  }

  ngOnInit(): void {
    this.layers = this.buildLayers();
  }


  ngAfterViewInit(): void {
    setTimeout(() => {
      this.buildMap();
    }, 100);
  }

  buildMap(): void {

    const fullScreen = new FullScreen();
    const attribution = new Attribution({
      collapsible: true
    });

    this.map = new Map({
      target: this.mapId,
      layers: this.layers.toArray(),
      controls: defaultControls({attribution: false}).extend([fullScreen, attribution]),
      view: new View({
        minZoom: ZoomLevel.vectorTileMinZoom,
        maxZoom: ZoomLevel.maxZoom
      })
    });

    this.fitBounds();
  }

  private buildLayers(): List<BaseLayer> {

    const osmLayer = OsmLayer.build();
    osmLayer.set("name", this.i18nService.translation("@@map.layer.osm"));

    const unchanged = this.segmentLayer("@@map.layer.unchanged", this.geometryDiff.common, 5, [0, 0, 255]);
    const added = this.segmentLayer("@@map.layer.added", this.geometryDiff.after, 12, [0, 255, 0]);
    const deleted = this.segmentLayer("@@map.layer.deleted", this.geometryDiff.before, 3, [255, 0, 0]);

    return List([
      osmLayer,
      unchanged,
      added,
      deleted,
      this.nodeLayer()
    ]).filter(layer => layer !== null);
  }

  private segmentLayer(name: string, segments: List<PointSegment>, width: number, color: Color): BaseLayer {
    if (segments.isEmpty()) {
      return null;
    }

    const style = new Style({
      stroke: new Stroke({
        color: color,
        width: width
      })
    });

    const source = new VectorSource();
    segments.forEach(segment => {
      const p1 = Util.latLonToCoordinate(segment.p1);
      const p2 = Util.latLonToCoordinate(segment.p2);
      const feature = new Feature(new LineString([p1, p2]));
      feature.setStyle(style);
      source.addFeature(feature);
    });

    const layer = new VectorLayer({
      source: source
    });
    layer.set("name", this.i18nService.translation(name));
    return layer;
  }

  private nodeLayer(): BaseLayer {

    if (this.nodes.isEmpty()) {
      return null;
    }

    const source = new VectorSource();
    this.nodes.forEach(node => {
      const after = Util.latLonToCoordinate(node);
      const nodeMarker = Marker.create("blue", after);
      source.addFeature(nodeMarker);
    });
    const layer = new VectorLayer({
      source: source
    });
    layer.set("name", this.i18nService.translation("@@map.layer.nodes"));
    return layer;
  }

  private fitBounds(): void {

    // note that the 'common' points are not taken into account here, so that we zoom in on the actual changes
    const points = this.geometryDiff.before.concat(this.geometryDiff.after);

    if (points.isEmpty()) {
      const southWest = fromLonLat([this.bounds.minLon, this.bounds.minLat]);
      const northEast = fromLonLat([this.bounds.maxLon, this.bounds.maxLat]);
      this.map.getView().fit(boundingExtent([southWest, northEast]));
    } else {
      const lattitudes = points.flatMap(s => List([s.p1.latitude, s.p2.latitude])).map(l => +l);
      const longitudes = points.flatMap(s => List([s.p1.longitude, s.p2.longitude])).map(l => +l);

      const latMin = lattitudes.min();
      const lonMin = longitudes.min();
      const latMax = lattitudes.max();
      const lonMax = longitudes.max();

      const latDelta = (latMax - latMin) / 8;
      const lonDelta = (lonMax - lonMin) / 8;

      const southWest = fromLonLat([lonMin - lonDelta, latMin - latDelta]);
      const northEast = fromLonLat([lonMax + lonDelta, latMax + latDelta]);

      this.map.getView().fit(boundingExtent([southWest, northEast]));
    }
  }
}
