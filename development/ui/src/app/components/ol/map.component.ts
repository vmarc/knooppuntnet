import {AfterViewInit, Component, Input} from '@angular/core';
import Coordinate from 'ol/coordinate';
import {click, pointerMove} from 'ol/events/condition';
import Select from 'ol/interaction/Select';
import TileLayer from 'ol/layer/Tile';
import VectorTileLayer from 'ol/layer/VectorTile';

import Map from 'ol/Map';
import {fromLonLat} from 'ol/proj';
import {Style} from 'ol/style';
import View from 'ol/View';
import Extent from 'ol/View';
import {PoiService} from "../../poi.service";
import {DebugLayer} from "./domain/debug-layer";
import {MainMapStyle} from "./domain/main-map-style";
import {MapClickHandler} from "./domain/map-click-handler";
import {MapMoveHandler} from "./domain/map-move-handler";
import {NetworkBitmapTileLayer} from "./domain/network-bitmap-tile-layer";
import {NetworkVectorTileLayer} from "./domain/network-vector-tile-layer";
import {OsmLayer} from "./domain/osm-layer";

import {ZoomLevel} from "./domain/zoom-level";
import {MapService} from "./map.service";
import {PoiTileLayerService} from "./poi-tile-layer.service";

@Component({
  selector: 'kpn-map',
  template: `
    <div id="{{id}}" class="map"></div>
  `,
  styles: [`
    .map {
      position: absolute;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
    }
  `]
})
export class MapComponent implements AfterViewInit {

  @Input() id;

  map: Map;
  mainMapStyle: (feature, resolution) => Style;

  bitmapTileLayer: TileLayer;
  vectorTileLayer: VectorTileLayer;
  poiTileLayer: VectorTileLayer;

  constructor(private mapService: MapService,
              private poiService: PoiService,
              private poiTileLayerService: PoiTileLayerService) {
  }

  ngAfterViewInit(): void {

    this.bitmapTileLayer = NetworkBitmapTileLayer.build(this.mapService.networkType.value);
    this.vectorTileLayer = NetworkVectorTileLayer.build(this.mapService.networkType.value);
    this.poiTileLayer = this.poiTileLayerService.buildLayer();

    this.map = new Map({
      target: this.id,
      layers: [
        OsmLayer.build(),
        this.poiTileLayer,
        this.bitmapTileLayer,
        this.vectorTileLayer,
        DebugLayer.build()
      ],
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.maxZoom
      })
    });

    this.mainMapStyle = new MainMapStyle(this.map, this.mapService).styleFunction();

    this.installClickInteraction();
    this.installMoveInteraction();

    const a: Coordinate = fromLonLat([2.24, 50.16]);
    const b: Coordinate = fromLonLat([10.56, 54.09]);
    const extent: Extent = [a[0], a[1], b[0], b[1]];
    const view = this.map.getView();
    view.fit(extent);
    view.on("change:resolution", () => this.zoom(view.getZoom()));

    this.mapService.networkType.subscribe(networkType => {
      this.bitmapTileLayer = NetworkBitmapTileLayer.build(networkType);
      this.vectorTileLayer = NetworkVectorTileLayer.build(networkType);
      this.vectorTileLayer.setStyle(this.mainMapStyle);
      this.map.getLayers().removeAt(2);
      this.map.getLayers().insertAt(2, this.bitmapTileLayer);
      this.map.getLayers().removeAt(3);
      this.map.getLayers().insertAt(3, this.vectorTileLayer);
      this.updateLayerVisibility(view.getZoom());
    });

  }

  updateSize() {
    if (this.map != null) {
      this.map.updateSize();
    }
  }

  private zoom(zoomLevel: number) {
    this.updateLayerVisibility(zoomLevel);
    this.poiService.updateZoomLevel(zoomLevel);
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
    if (zoom >= 11) {
      this.poiTileLayer.setVisible(true);
    } else if (zoom >= ZoomLevel.vectorTileMinZoom) {
      this.poiTileLayer.setVisible(false);
    }
  }

  private installClickInteraction() {
    const interaction = new Select({
      condition: click,
      multi: false,
      style: new Style() // this overrides the normal openlayers default edit style
    });
    interaction.on("select", (e) => {
      new MapClickHandler(this.mapService).handle(e);
      this.vectorTileLayer.changed();
      return true;
    });
    this.map.addInteraction(interaction);

  }

  private installMoveInteraction() {
    const interaction = new Select({
      condition: pointerMove,
      multi: false,
      style: new Style() // this overrides the normal openlayers default edit style
    });
    interaction.on("select", (e) => {
      new MapMoveHandler(this.map, this.mapService).handle(e);
      this.vectorTileLayer.changed();
      return true;
    });
    this.map.addInteraction(interaction);
  }

}
