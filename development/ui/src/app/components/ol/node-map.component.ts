import {AfterViewInit, Component, Input} from "@angular/core";
import {Attribution, defaults as defaultControls} from 'ol/control';
import {click} from "ol/events/condition";
import Feature from "ol/Feature";
import Point from "ol/geom/Point";
import Select from "ol/interaction/Select";
import TileLayer from "ol/layer/Tile";
import VectorLayer from "ol/layer/Vector";
import VectorTileLayer from "ol/layer/VectorTile";
import Map from "ol/Map";
import VectorSource from "ol/source/Vector";
import Icon from "ol/style/Icon";
import Style from "ol/style/Style";
import View from "ol/View";
import {NetworkTypes} from "../../kpn/common/network-types";
import {NodeInfo} from "../../kpn/shared/node-info";
import {Util} from "../shared/util";
import {DebugLayer} from "./domain/debug-layer";
import {NetworkBitmapTileLayer} from "./domain/network-bitmap-tile-layer";
import {NetworkVectorTileLayer} from "./domain/network-vector-tile-layer";
import {NodeMapStyle} from "./domain/node-map-style";
import {OsmLayer} from "./domain/osm-layer";
import {ZoomLevel} from "./domain/zoom-level";

@Component({
  selector: "kpn-node-map",
  template: `
    <div id="node-map" class="map"></div>
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
export class NodeMapComponent implements AfterViewInit {

  @Input() nodeInfo: NodeInfo;

  map: Map;
  nodeMapStyle: (feature, resolution) => Style;

  bitmapTileLayer: TileLayer;
  vectorTileLayer: VectorTileLayer;

  constructor() {
  }

  ngAfterViewInit(): void {

    this.bitmapTileLayer = NetworkBitmapTileLayer.build(NetworkTypes.hiking);
    this.vectorTileLayer = NetworkVectorTileLayer.build(NetworkTypes.hiking);

    const attribution = new Attribution({
      collapsible: false
    });

    const center = Util.toCoordinate(this.nodeInfo.latitude, this.nodeInfo.longitude);

    this.map = new Map({
      declutter: true,
      target: "node-map",
      layers: [
        OsmLayer.build(),
        this.bitmapTileLayer,
        this.vectorTileLayer,
        this.buildMarkerLayer(),
        DebugLayer.build()
      ],
      controls: defaultControls({attribution: false}).extend([attribution]),
      view: new View({
        center: center,
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.maxZoom,
        zoom: 18
      })
    });

    this.nodeMapStyle = new NodeMapStyle(this.map).styleFunction();

    // this.installClickInteraction();

    const view = this.map.getView();
    view.on("change:resolution", () => this.zoom(view.getZoom()));

    this.vectorTileLayer.setStyle(this.nodeMapStyle);
    this.updateLayerVisibility(view.getZoom());
  }

  updateSize() {
    if (this.map != null) {
      this.map.updateSize();
    }
  }

  private zoom(zoomLevel: number) {
    this.updateLayerVisibility(zoomLevel);
    return true;
  }

  private buildMarkerLayer() {

    // TODO move to separate Marker class, also use in planner-route-layer-impl.ts
    const style = new Style({
      image: new Icon({
        anchor: [12, 41],
        anchorXUnits: "pixels",
        anchorYUnits: "pixels",
        src: "assets/images/marker-icon-blue.png"
      })
    });

    const coordinate = Util.toCoordinate(this.nodeInfo.latitude, this.nodeInfo.longitude);
    const feature = new Feature(new Point(coordinate));
    feature.setStyle(style);


    const source = new VectorSource();
    const layer = new VectorLayer({
      source: source
    });

    source.addFeature(feature);
    layer.set("name", "Node"); // TODO translate: "Knooppunt"
    return layer;
  }

  private updateLayerVisibility(zoomLevel: number) {

    console.log(`DEBUG NodeMapComponent updateLayerVisibility zoomLevel=${zoomLevel}`);


    const zoom = Math.round(zoomLevel);
    if (zoom <= ZoomLevel.bitmapTileMaxZoom) {
      this.bitmapTileLayer.setVisible(true);
      this.vectorTileLayer.setVisible(false);
    } else if (zoom >= ZoomLevel.vectorTileMinZoom) {
      this.bitmapTileLayer.setVisible(false);
      this.vectorTileLayer.setVisible(true);
    }
  }

  private installClickInteraction() {
    const interaction = new Select({
      condition: click,
      multi: false,
      style: new Style() // this overrides the normal openlayers default edit style
    });
    interaction.on("select", (e) => {
      // new MapClickHandler(this.mapService).handle(e);
      this.vectorTileLayer.changed();
      return true;
    });
    this.map.addInteraction(interaction);

  }

}
