import {AfterViewInit, Component, Input} from "@angular/core";
import {Attribution, defaults as defaultControls} from 'ol/control';
import TileLayer from "ol/layer/Tile";
import VectorTileLayer from "ol/layer/VectorTile";
import Map from "ol/Map";
import View from "ol/View";
import {RouteInfo} from "../../kpn/shared/route/route-info";
import {DebugLayer} from "./domain/debug-layer";
import {NodeMapStyle} from "./domain/node-map-style";
import {OsmLayer} from "./domain/osm-layer";
import {ZoomLevel} from "./domain/zoom-level";
import {MapClickService} from "./map-click.service";
import {RouteMapBuilder} from "./route-map-builder";

@Component({
  selector: "kpn-route-map",
  template: `
    <div id="route-map" class="map"></div>
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
export class RouteMapComponent implements AfterViewInit {

  @Input() routeInfo: RouteInfo;

  map: Map;

  bitmapTileLayer: TileLayer;
  vectorTileLayer: VectorTileLayer;

  constructor(private mapClickService: MapClickService) {
  }

  ngAfterViewInit(): void {

    const routeMapBuilder = new RouteMapBuilder(this.routeInfo);

    const attribution = new Attribution({
      collapsible: false
    });

    this.map = new Map({
      declutter: true,
      target: "node-map",
      layers: [
        OsmLayer.build(),
        this.bitmapTileLayer,
        this.vectorTileLayer,
        DebugLayer.build()
      ],
      controls: defaultControls({attribution: false}).extend([attribution]),
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.maxZoom
      })
    });

    this.map.addInteraction(this.mapClickService.createInteraction());

    const view = this.map.getView();
    view.fit(routeMapBuilder.buildExtent());
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

  private zoom(zoomLevel: number) {
    this.updateLayerVisibility(zoomLevel);
    return true;
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

}
