import {Input} from "@angular/core";
import {Component} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {List} from "immutable";
import {Collection} from "ol";
import {boundingExtent} from "ol/extent";
import BaseLayer from "ol/layer/Base";
import LayerGroup from "ol/layer/Group";
import TileLayer from "ol/layer/Tile";
import VectorTileLayer from "ol/layer/VectorTile";
import Map from "ol/Map";
import {fromLonLat} from "ol/proj";
import {StyleFunction} from "ol/style/Style";
import View from "ol/View";
import {I18nService} from "../../i18n/i18n.service";
import {Bounds} from "../../kpn/api/common/bounds";
import {NetworkType} from "../../kpn/api/custom/network-type";
import {Subscriptions} from "../../util/Subscriptions";
import {PageService} from "../shared/page.service";
import {MainMapStyle} from "./domain/main-map-style";
import {NetworkBitmapTileLayer} from "./domain/network-bitmap-tile-layer";
import {NetworkVectorTileLayer} from "./domain/network-vector-tile-layer";
import {ZoomLevel} from "./domain/zoom-level";
import {MapControls} from "./layers/map-controls";
import {MapClickService} from "./map-click.service";
import {MapLayerService} from "./map-layer.service";
import {MapService} from "./map.service";

@Component({
  selector: "kpn-location-map",
  template: `
    <div id="location-map" class="map">
      <kpn-layer-switcher [layers]="layers"></kpn-layer-switcher>
    </div>
  `,
  styles: [`
    .map {
      position: absolute;
      top: 183px;
      left: 0;
      right: 0;
      bottom: 0;
      background-color: white;
    }
  `]
})
export class LocationMapComponent {

  @Input() networkType: NetworkType;
  @Input() bounds: Bounds;
  @Input() geoJson: string;

  map: Map;
  mainMapStyle: StyleFunction;

  layers: List<BaseLayer> = List();
  bitmapTileLayer: TileLayer;
  vectorTileLayer: VectorTileLayer;

  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private pageService: PageService,
              private mapService: MapService,
              private mapClickService: MapClickService,
              private mapLayerService: MapLayerService,
              private i18nService: I18nService) {
    this.pageService.showFooter = false;
  }

  ngOnInit(): void {
    this.layers = this.buildLayers();
    this.subscriptions.add(this.pageService.sidebarOpen.subscribe(state => {
      if (this.map) {
        setTimeout(() => this.map.updateSize(), 250);
      }
    }));
  }

  ngAfterViewInit(): void {

    this.map = new Map({
      target: "location-map",
      layers: this.layers.toArray(),
      controls: MapControls.build(),
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.vectorTileMaxOverZoom
      })
    });

    this.mainMapStyle = new MainMapStyle(this.map, this.mapService).styleFunction();

    const view = this.map.getView();

    view.on("change:resolution", () => this.zoom(view.getZoom()));

    this.vectorTileLayer.setStyle(this.mainMapStyle);
    this.updateLayerVisibility(view.getZoom());

    const southWest = fromLonLat([this.bounds.minLon, this.bounds.minLat]);
    const northEast = fromLonLat([this.bounds.maxLon, this.bounds.maxLat]);
    this.map.getView().fit(boundingExtent([southWest, northEast]));

    this.mapClickService.installOn(this.map);
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
    this.subscriptions.unsubscribe();
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

  private buildLayers(): List<BaseLayer> {

    this.bitmapTileLayer = NetworkBitmapTileLayer.build(this.networkType);
    this.vectorTileLayer = NetworkVectorTileLayer.build(this.networkType);

    const layerGroup = new LayerGroup();
    layerGroup.setLayers(new Collection([this.bitmapTileLayer, this.vectorTileLayer]));
    const layerGroupName = this.i18nService.translation("@@map.layer.nodes-and-routes");
    layerGroup.set("name", layerGroupName);

    const layerArray: Array<BaseLayer> = [];
    layerArray.push(this.mapLayerService.osmLayer());
    layerArray.push(layerGroup);
    layerArray.push(this.mapLayerService.locationBoundaryLayer(this.geoJson));
    return List(layerArray);
  }
}
