import {Input} from "@angular/core";
import {Component} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {List} from "immutable";
import {Collection} from "ol";
import {defaults as defaultControls} from "ol/control";
import {Attribution} from "ol/control";
import {ScaleLine} from "ol/control";
import {FullScreen} from "ol/control";
import {boundingExtent} from "ol/extent";
import {GeoJSON} from "ol/format";
import BaseLayer from "ol/layer/Base";
import LayerGroup from "ol/layer/Group";
import TileLayer from "ol/layer/Tile";
import VectorLayer from "ol/layer/Vector";
import VectorTileLayer from "ol/layer/VectorTile";
import Map from "ol/Map";
import {fromLonLat} from "ol/proj";
import VectorSource from "ol/source/Vector";
import {Fill} from "ol/style";
import {Style} from "ol/style";
import {Stroke} from "ol/style";
import {StyleFunction} from "ol/style/Style";
import View from "ol/View";
import {Observable} from "rxjs";
import {I18nService} from "../../i18n/i18n.service";
import {Bounds} from "../../kpn/api/common/bounds";
import {NetworkType} from "../../kpn/api/custom/network-type";
import {Subscriptions} from "../../util/Subscriptions";
import {PageService} from "../shared/page.service";
import {MainMapStyle} from "./domain/main-map-style";
import {NetworkBitmapTileLayer} from "./domain/network-bitmap-tile-layer";
import {NetworkVectorTileLayer} from "./domain/network-vector-tile-layer";
import {OsmLayer} from "./domain/osm-layer";
import {ZoomLevel} from "./domain/zoom-level";
import {MapClickService} from "./map-click.service";
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

  progress: Observable<number>;

  map: Map;
  mainMapStyle: StyleFunction;

  layers: List<BaseLayer> = List();
  bitmapTileLayer: TileLayer;
  vectorTileLayer: VectorTileLayer;
  geoJsonLayer: VectorLayer;

  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private pageService: PageService,
              private mapService: MapService,
              private mapClickService: MapClickService,
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


    const fullScreen = new FullScreen();
    const scaleLine = new ScaleLine();
    const attribution = new Attribution({
      collapsible: false
    });

    this.map = new Map({
      target: "location-map",
      layers: this.layers.toArray(),
      controls: defaultControls({attribution: false}).extend([fullScreen, scaleLine, attribution]),
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

    const features = (new GeoJSON()).readFeatures(this.geoJson, {featureProjection: "EPSG:3857"});

    const vectorSource = new VectorSource({
      features: features
    });

    const locationStyle = new Style({
        stroke: new Stroke({
          color: "rgba(255, 0, 0, 0.9)",
          width: 3
        }),
        fill: new Fill({
          color: "rgba(255, 0, 0, 0.05)"
        })
      }
    );

    const styleFunction = function (feature) {
      return locationStyle;
    };

    this.geoJsonLayer = new VectorLayer({
      source: vectorSource,
      style: styleFunction
    });

    const geoJsonLayerName = this.i18nService.translation("@@map.layer.boundary");
    this.geoJsonLayer.set("name", geoJsonLayerName);

    this.bitmapTileLayer = NetworkBitmapTileLayer.build(this.networkType);
    this.vectorTileLayer = NetworkVectorTileLayer.build(this.networkType);

    const layerGroup = new LayerGroup();
    layerGroup.setLayers(new Collection([this.bitmapTileLayer, this.vectorTileLayer]));
    const layerGroupName = this.i18nService.translation("@@map.layer.nodes-and-routes");
    layerGroup.set("name", layerGroupName);

    const osmLayer = OsmLayer.build();
    const osmLayerName = this.i18nService.translation("@@map.layer.osm");
    osmLayer.set("name", osmLayerName);


    const layerArray: Array<BaseLayer> = [];
    layerArray.push(osmLayer);
    layerArray.push(layerGroup);
    layerArray.push(this.geoJsonLayer);

    return List(layerArray);
  }
}
