import {AfterViewInit, Component, Input} from '@angular/core';

import Map from 'ol/Map';
import View from 'ol/View';
import Coordinate from 'ol/View';
import Extent from 'ol/View';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import XYZ from 'ol/source/XYZ';
import TileDebug from 'ol/source/TileDebug';
import MVT from 'ol/format/MVT';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import {fromLonLat} from 'ol/proj';
import {createXYZ} from 'ol/tilegrid';
import {click, pointerMove} from 'ol/events/condition';
import Feature from 'ol/Feature';
import Select from 'ol/interaction/Select';
import {Icon, Style} from 'ol/style';

import {ZoomLevel} from "./domain/zoom-level";
import {MainMapStyle} from "./domain/main-map-style";
import {MapClickHandler} from "./domain/map-click-handler";
import {MapMoveHandler} from "./domain/map-move-handler";
import {MapService} from "./map.service";
import {PoiStyle} from "./domain/poi-style";

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

  poiIconStyleMap: PoiStyle;

  constructor(private mapService: MapService) {
    mapService.poiConfiguration.subscribe(configuration => {
      if (configuration !== null) {
        this.poiIconStyleMap = new PoiStyle(configuration)
      }
    });
  }

  ngAfterViewInit(): void {

    this.bitmapTileLayer = this.buildBitmapTileLayer();
    this.vectorTileLayer = this.buildVectorTileLayer();
    this.poiTileLayer = this.buildPoiTileLayer();
    this.poiTileLayer.setStyle(this.poiStyleFunction());

    this.map = new Map({
      target: this.id,
      layers: [
        this.osmLayer(),
        this.poiTileLayer,
        this.bitmapTileLayer,
        this.vectorTileLayer,
        this.debugLayer()
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
      this.bitmapTileLayer = this.buildBitmapTileLayer();
      this.vectorTileLayer = this.buildVectorTileLayer();
      this.vectorTileLayer.setStyle(this.mainMapStyle);
      this.map.getLayers().removeAt(2);
      this.map.getLayers().insertAt(2, this.bitmapTileLayer);
      this.map.getLayers().removeAt(3);
      this.map.getLayers().insertAt(3, this.vectorTileLayer);
      this.updateLayerVisibility(view.getZoom());
    });

  }

  private poiStyleFunction() {
    return (feature, resolution) => {
      if (this.poiIconStyleMap) {
        const layer = feature.get("layer");
        if (layer) {
          const style = this.poiIconStyleMap.get(layer);
          if (style) {
            return [style];
          }
        }
      }
      return null;
    };
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
    if (zoom >= 11) {
      this.poiTileLayer.setVisible(true);
    } else if (zoom >= ZoomLevel.vectorTileMinZoom) {
      this.poiTileLayer.setVisible(false);
    }
  }

  private osmLayer() {
    const osmLayer = new TileLayer({
      source: new OSM()
    });
    osmLayer.set("name", "OpenStreetMap");
    return osmLayer;
  }

  private buildBitmapTileLayer() {
    return new TileLayer({
      source: new XYZ({
        minZoom: ZoomLevel.bitmapTileMinZoom,
        maxZoom: ZoomLevel.bitmapTileMaxZoom,
        url: "/tiles/" + this.mapService.networkType.value.name + "/{z}/{x}/{y}.png"
      })
    });
  }

  private debugLayer() {
    return new TileLayer({
      source: new TileDebug({
        projection: "EPSG:3857",
        tileGrid: new OSM().getTileGrid()
      })
    });
  }

  private buildVectorTileLayer() {

    const format = new MVT({
      featureClass: Feature // this is important to avoid error upon first selection in the map
    });

    const networkType = this.mapService.networkType;

    const urlFunction = function (tileCoord, pixelRatio, projection) {
      const zIn = tileCoord[0];
      const xIn = tileCoord[1];
      const yIn = tileCoord[2];

      const z = zIn >= ZoomLevel.vectorTileMaxZoom ? ZoomLevel.vectorTileMaxZoom : zIn;
      const x = xIn;
      const y = -yIn - 1;
      return "/tiles/" + networkType.value.name + "/" + z + "/" + x + "/" + y + ".mvt"
    };

    const tileGrid = createXYZ({
      // minZoom: ZoomLevel.vectorTileMinZoom
      // maxZoom: ZoomLevel.vectorTileMaxOverZoom
    });

    const source = new VectorTile({
      format: format,
      tileGrid: tileGrid,
      tileUrlFunction: urlFunction
    });

    const layer = new VectorTileLayer({
      source: source
    });

    return layer;
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

  private buildPoiTileLayer() {

    const format = new MVT({
      featureClass: Feature // this is important to avoid error upon first selection in the map
    });

    const urlFunction = function (tileCoord, pixelRatio, projection) {
      const zIn = tileCoord[0];
      const xIn = tileCoord[1];
      const yIn = tileCoord[2];

      const z = zIn >= ZoomLevel.vectorTileMaxZoom ? ZoomLevel.vectorTileMaxZoom : zIn;
      const x = xIn;
      const y = -yIn - 1;
      return "/tiles/poi/" + z + "/" + x + "/" + y + ".mvt"
    };

    const tileGrid = createXYZ({
      // minZoom: ZoomLevel.vectorTileMinZoom
      // maxZoom: ZoomLevel.vectorTileMaxOverZoom
    });

    const source = new VectorTile({
      format: format,
      tileGrid: tileGrid,
      tileUrlFunction: urlFunction
    });

    const layer = new VectorTileLayer({
      source: source
    });

    return layer;
  }

}
