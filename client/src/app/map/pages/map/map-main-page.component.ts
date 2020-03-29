import {AfterViewInit, Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {ScaleLine} from "ol/control";
import {FullScreen} from "ol/control";
import {Attribution, defaults as defaultControls} from "ol/control";
import TileLayer from "ol/layer/Tile";
import VectorTileLayer from "ol/layer/VectorTile";
import Map from "ol/Map";
import Overlay from "ol/Overlay";
import {StyleFunction} from "ol/style/Style";
import View from "ol/View";
import {asyncScheduler, Observable} from "rxjs";
import {throttleTime} from "rxjs/operators";
import {MainMapStyle} from "../../../components/ol/domain/main-map-style";
import {MapGeocoder} from "../../../components/ol/domain/map-geocoder";
import {NetworkBitmapTileLayer} from "../../../components/ol/domain/network-bitmap-tile-layer";
import {NetworkVectorTileLayer} from "../../../components/ol/domain/network-vector-tile-layer";
import {OsmLayer} from "../../../components/ol/domain/osm-layer";
import {ZoomLevel} from "../../../components/ol/domain/zoom-level";
import {MapPositionService} from "../../../components/ol/map-position.service";
import {MapService} from "../../../components/ol/map.service";
import {PoiTileLayerService} from "../../../components/ol/poi-tile-layer.service";
import {TileLoadProgressService} from "../../../components/ol/tile-load-progress.service";
import {PageService} from "../../../components/shared/page.service";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {PoiService} from "../../../services/poi.service";
import {Subscriptions} from "../../../util/Subscriptions";
import {PlannerService} from "../../planner.service";
import {PlannerInteraction} from "../../planner/interaction/planner-interaction";

@Component({
  selector: "kpn-map-main-page",
  template: `
    <kpn-map-popup></kpn-map-popup>
    <mat-progress-bar class="progress" mode="determinate" [value]="progress | async"></mat-progress-bar>
    <div id="main-map" class="map"></div>
  `,
  styles: [`
    .progress {
      position: absolute;
      top: 48px;
      left: 0;
      right: 0;
    }

    .map {
      position: absolute;
      top: 52px;
      left: 0;
      right: 0;
      bottom: 0;
      background-color: white;
    }

  `]
})
export class MapMainPageComponent implements OnInit, OnDestroy, AfterViewInit {

  map: Map;
  mainMapStyle: StyleFunction;

  progress: Observable<number>;
  bitmapTileLayer: TileLayer;
  vectorTileLayer: VectorTileLayer;
  poiTileLayer: VectorTileLayer;
  interaction = new PlannerInteraction(this.plannerService.engine);
  overlay: Overlay;

  private readonly subscriptions = new Subscriptions();
  private lastKnownSidebarOpen = false;

  constructor(private activatedRoute: ActivatedRoute,
              private pageService: PageService,
              private mapService: MapService,
              private poiService: PoiService,
              private poiTileLayerService: PoiTileLayerService,
              private plannerService: PlannerService,
              private tileLoadProgressService: TileLoadProgressService,
              private mapPositionService: MapPositionService) {
    this.pageService.showFooter = false;
    this.progress = tileLoadProgressService.progress.pipe(throttleTime(200, asyncScheduler, {trailing: true}));
  }

  ngOnInit(): void {

    this.lastKnownSidebarOpen = this.pageService.sidebarOpen.value;

    this.subscriptions.add(this.activatedRoute.params.subscribe(params => {
      const networkTypeName = params["networkType"];
      const networkType = NetworkType.withName(networkTypeName);
      this.mapService.networkType.next(networkType);
    }));

    this.subscriptions.add(this.pageService.sidebarOpen.subscribe(state => {
      if (this.map) {
        setTimeout(() => this.map.updateSize(), 250);
      }
    }));

  }

  ngAfterViewInit(): void {

    this.overlay = new Overlay({
      id: "popup",
      element: document.getElementById("popup"),
      autoPan: true,
      autoPanAnimation: {
        duration: 250
      }
    });

    this.bitmapTileLayer = NetworkBitmapTileLayer.build(this.mapService.networkType.value);
    this.vectorTileLayer = NetworkVectorTileLayer.build(this.mapService.networkType.value);
    this.poiTileLayer = this.poiTileLayerService.buildLayer();
    this.poiTileLayer.setVisible(false);

    const fullScreen = new FullScreen();
    const scaleLine = new ScaleLine();
    const attribution = new Attribution({
      collapsible: false
    });

    this.map = new Map({
      target: "main-map",
      layers: [
        OsmLayer.build(),
        // DebugLayer.build(),
        this.poiTileLayer,
        this.bitmapTileLayer,
        this.vectorTileLayer
      ],
      overlays: [this.overlay],
      controls: defaultControls({attribution: false}).extend([fullScreen, scaleLine, attribution]),
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.vectorTileMaxOverZoom
      })
    });

    this.mainMapStyle = new MainMapStyle(this.map, this.mapService).styleFunction();

    this.plannerService.init(this.map);
    this.plannerService.context.setNetworkType(this.mapService.networkType.value);
    this.interaction.addToMap(this.map);

    const view = this.map.getView();
    this.tileLoadProgressService.install(this.bitmapTileLayer, this.vectorTileLayer, this.poiTileLayer);
    this.mapPositionService.install(view);
    this.poiService.updateZoomLevel(view.getZoom());

    view.on("change:resolution", () => this.zoom(view.getZoom()));

    this.vectorTileLayer.setStyle(this.mainMapStyle);
    this.updateLayerVisibility(view.getZoom());

    MapGeocoder.install(this.map);
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
    this.subscriptions.unsubscribe();
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
    this.poiTileLayer.setVisible(zoom >= ZoomLevel.poiTileMinZoom);
  }

}
