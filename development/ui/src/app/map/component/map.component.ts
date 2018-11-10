import {AfterViewInit, Component, EventEmitter, Input, Output} from '@angular/core';

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
import Style from 'ol/style/Style';

import {ZoomLevel} from "../domain/zoom-level";
import {MainMapStyle} from "../domain/main-map-style";
import {MapClickHandler} from "../domain/map-click-handler";
import {SelectedFeatureHolder} from "../domain/selected-feature-holder";
import {MapState} from "../domain/map-state";
import {MapMoveHandler} from "../domain/map-move-handler";
import {SelectedFeature} from "../domain/selected-feature";

@Component({
  selector: 'kpn-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements AfterViewInit {

  @Input() id;
  @Input() networkType: string;

  @Output() featureSelection = new EventEmitter<SelectedFeature>();

  map: Map;
  mapState: MapState;
  mainMapStyle: (feature, resolution) => Style;
  selectionHolder: SelectedFeatureHolder;

  bitmapTileLayer: TileLayer;
  vectorTileLayer: VectorTileLayer;
  poiTileLayer: VectorTileLayer;

  constructor() {
  }

  ngAfterViewInit(): void {

    this.bitmapTileLayer = this.buildBitmapTileLayer();
    this.vectorTileLayer = this.buildVectorTileLayer();
    this.poiTileLayer = this.buildPoiTileLayer();

    this.map = new Map({
      target: this.id,
      layers: [
        this.osmLayer(),
        this.bitmapTileLayer,
        this.vectorTileLayer,
        this.poiTileLayer,
        this.debugLayer()
      ],
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.maxZoom
      })
    });

    this.mapState = new MapState();
    this.mainMapStyle = new MainMapStyle(this.map, this.mapState).styleFunction();
    this.repaintVectorTileLayer();
    this.selectionHolder = new SelectedFeatureHolder(selectedFeature => {
      this.featureSelection.emit(selectedFeature);
      this.repaintVectorTileLayer();
    });

    this.installClickInteraction();
    this.installMoveInteraction();

    const a: Coordinate = fromLonLat([2.24, 50.16]);
    const b: Coordinate = fromLonLat([10.56, 54.09]);
    const extent: Extent = [a[0], a[1], b[0], b[1]];
    const view = this.map.getView();
    view.fit(extent);
    view.on("change:resolution", () => this.zoom(view.getZoom()));
  }

  private zoom(zoomLevel: number) {
    const zoom = Math.round(zoomLevel);
    if (zoom <= ZoomLevel.bitmapTileMaxZoom) {
      this.bitmapTileLayer.setVisible(true);
      this.vectorTileLayer.setVisible(false);
    }
    else if (zoom >= ZoomLevel.vectorTileMinZoom) {
      this.bitmapTileLayer.setVisible(false);
      this.vectorTileLayer.setVisible(true);
    }
    if (zoom >= 11) {
      this.poiTileLayer.setVisible(true);
    }
    else if (zoom >= ZoomLevel.vectorTileMinZoom) {
      this.poiTileLayer.setVisible(false);
    }

    return true;
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
        url: "/tiles/" + this.networkType + "/{z}/{x}/{y}.png"
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

    const tileType = this.networkType;

    const urlFunction = function (tileCoord, pixelRatio, projection) {
      const zIn = tileCoord[0];
      const xIn = tileCoord[1];
      const yIn = tileCoord[2];

      const z = zIn >= ZoomLevel.vectorTileMaxZoom ? ZoomLevel.vectorTileMaxZoom : zIn;
      const x = xIn;
      const y = -yIn - 1;
      return "/tiles/" + tileType + "/" + z + "/" + x + "/" + y + ".mvt"
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
      new MapClickHandler(this.mapState, this.selectionHolder).handle(e);
      this.repaintVectorTileLayer();
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
      new MapMoveHandler(this.map, this.mapState, this.selectionHolder).handle(e);
      this.repaintVectorTileLayer();
      return true;
    });
    this.map.addInteraction(interaction);
  }

  private repaintVectorTileLayer() {
    this.vectorTileLayer.setStyle(this.mainMapStyle);
  }

  private buildPoiTileLayer() {

    const format = new MVT({
      featureClass: Feature // this is important to avoid error upon first selection in the map
    });

    const tileType = "poi";

    const urlFunction = function (tileCoord, pixelRatio, projection) {
      const zIn = tileCoord[0];
      const xIn = tileCoord[1];
      const yIn = tileCoord[2];

      const z = zIn >= ZoomLevel.vectorTileMaxZoom ? ZoomLevel.vectorTileMaxZoom : zIn;
      const x = xIn;
      const y = -yIn - 1;
      return "/tiles/" + tileType + "/" + z + "/" + x + "/" + y + ".mvt"
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
