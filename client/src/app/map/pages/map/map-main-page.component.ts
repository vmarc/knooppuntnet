import {AfterViewInit, Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {throttleTime} from "rxjs/operators";
import {asyncScheduler, Observable} from "rxjs";
import {Attribution, defaults as defaultControls} from "ol/control";
import {click, pointerMove} from "ol/events/condition";
import Select from "ol/interaction/Select";
import TileLayer from "ol/layer/Tile";
import VectorTileLayer from "ol/layer/VectorTile";
import Map from "ol/Map";
import Style from "ol/style/Style";
import View from "ol/View";
import {MainMapStyle} from "../../../components/ol/domain/main-map-style";
import {MapClickHandler} from "../../../components/ol/domain/map-click-handler";
import {MapGeocoder} from "../../../components/ol/domain/map-geocoder";
import {MapMoveHandler} from "../../../components/ol/domain/map-move-handler";
import {NetworkBitmapTileLayer} from "../../../components/ol/domain/network-bitmap-tile-layer";
import {NetworkVectorTileLayer} from "../../../components/ol/domain/network-vector-tile-layer";
import {OsmLayer} from "../../../components/ol/domain/osm-layer";
import {ZoomLevel} from "../../../components/ol/domain/zoom-level";
import {MapService} from "../../../components/ol/map.service";
import {PoiTileLayerService} from "../../../components/ol/poi-tile-layer.service";
import {PageService} from "../../../components/shared/page.service";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {PoiService} from "../../../services/poi.service";
import {Subscriptions} from "../../../util/Subscriptions";
import {PlannerService} from "../../planner.service";
import {PlannerInteraction} from "../../planner/interaction/planner-interaction";
import {DebugLayer} from "../../../components/ol/domain/debug-layer";
import {TileLoadProgressService} from "../../../components/ol/tile-load-progress.service";
import {MapPositionService} from "../../../components/ol/map-position.service";

@Component({
  selector: "kpn-map-main-page",
  template: `
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
    }
  `]
})
export class MapMainPageComponent implements OnInit, OnDestroy, AfterViewInit {

  map: Map;
  mainMapStyle: (feature, resolution) => Style;

  progress: Observable<number>;
  bitmapTileLayer: TileLayer;
  vectorTileLayer: VectorTileLayer;
  poiTileLayer: VectorTileLayer;
  interaction = new PlannerInteraction(this.plannerService.engine);

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

    this.subscriptions.add(this.mapService.selectedFeature.subscribe(selectedFeature => {
      if (selectedFeature == null) {
        console.log("DEBUG MapMainPageComponent selectedFeature null");
      } else {
        console.log("DEBUG MapMainPageComponent selectedFeature type=" + selectedFeature.featureType + ", id=" + selectedFeature.featureId + ", name=" + selectedFeature.name);
      }
    }));

    this.subscriptions.add(this.pageService.sidebarOpen.subscribe(state => {
      if (this.map) {
        setTimeout(() => this.map.updateSize(), 250);
      }
    }));

  }

  ngAfterViewInit(): void {

    this.bitmapTileLayer = NetworkBitmapTileLayer.build(this.mapService.networkType.value);
    this.vectorTileLayer = NetworkVectorTileLayer.build(this.mapService.networkType.value);
    this.poiTileLayer = this.poiTileLayerService.buildLayer();
    this.poiTileLayer.setVisible(false);

    const attribution = new Attribution({
      collapsible: false
    });

    this.map = new Map({
      declutter: true,
      target: "main-map",
      layers: [
        OsmLayer.build(),
        DebugLayer.build(),
        this.poiTileLayer,
        this.bitmapTileLayer,
        this.vectorTileLayer
      ],
      controls: defaultControls({attribution: false}).extend([attribution]),
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.maxZoom
      })
    });

    this.mainMapStyle = new MainMapStyle(this.map, this.mapService).styleFunction();

    this.plannerService.init(this.map);
    this.plannerService.context.setNetworkType(this.mapService.networkType.value);
    this.interaction.addToMap(this.map);

    // this.installClickInteraction();
    // this.installMoveInteraction();
    const view = this.map.getView();
    this.tileLoadProgressService.install(this.bitmapTileLayer, this.vectorTileLayer, this.poiTileLayer);
    this.mapPositionService.install(view);

    view.on("change:resolution", () => this.zoom(view.getZoom()));

    this.vectorTileLayer.setStyle(this.mainMapStyle);
    this.updateLayerVisibility(this.map.getView().getZoom());

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
    if (zoom >= 11) {
      this.poiTileLayer.setVisible(true);
    } else {
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
