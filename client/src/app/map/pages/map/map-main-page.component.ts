import {AfterViewInit, Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {List} from "immutable";
import Map from "ol/Map";
import Overlay from "ol/Overlay";
import View from "ol/View";
import {asyncScheduler, Observable} from "rxjs";
import {throttleTime} from "rxjs/operators";
import {MapGeocoder} from "../../../components/ol/domain/map-geocoder";
import {ZoomLevel} from "../../../components/ol/domain/zoom-level";
import {MapControls} from "../../../components/ol/layers/map-controls";
import {MapLayer} from "../../../components/ol/layers/map-layer";
import {MapLayers} from "../../../components/ol/layers/map-layers";
import {MapLayerService} from "../../../components/ol/services/map-layer.service";
import {MapPositionService} from "../../../components/ol/services/map-position.service";
import {MapService} from "../../../components/ol/services/map.service";
import {PoiTileLayerService} from "../../../components/ol/services/poi-tile-layer.service";
import {TileLoadProgressService} from "../../../components/ol/services/tile-load-progress.service";
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
    <div id="main-map" class="map">
      <kpn-layer-switcher [mapLayers]="layers"></kpn-layer-switcher>
    </div>
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

  layers: MapLayers;
  progress: Observable<number>;
  interaction = new PlannerInteraction(this.plannerService.engine);
  overlay: Overlay;
  private map: Map;
  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private pageService: PageService,
              private mapService: MapService,
              private mapLayerService: MapLayerService,
              private poiService: PoiService,
              private poiTileLayerService: PoiTileLayerService,
              private plannerService: PlannerService,
              private tileLoadProgressService: TileLoadProgressService,
              private mapPositionService: MapPositionService) {
    this.pageService.showFooter = false;
    this.progress = tileLoadProgressService.progress.pipe(throttleTime(200, asyncScheduler, {trailing: true}));
  }

  ngOnInit(): void {

    this.subscriptions.add(this.activatedRoute.params.subscribe(params => {
      const networkTypeName = params["networkType"];
      const networkType = NetworkType.withName(networkTypeName);
      this.mapService.networkType.next(networkType);
      this.layers = this.buildLayers();
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

    this.map = new Map({
      target: "main-map",
      layers: this.layers.toArray(),
      overlays: [this.overlay],
      controls: MapControls.build(),
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.vectorTileMaxOverZoom
      })
    });
    this.layers.applyMap(this.map);

    this.plannerService.init(this.map);
    this.plannerService.context.setNetworkType(this.mapService.networkType.value);
    this.interaction.addToMap(this.map);

    const view = this.map.getView();
    // TODO this.tileLoadProgressService.install(this.bitmapTileLayer, this.vectorTileLayer, this.poiTileLayer);
    this.mapPositionService.install(view);
    this.poiService.updateZoomLevel(view.getZoom());

    MapGeocoder.install(this.map);
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
    this.subscriptions.unsubscribe();
  }

  private buildLayers(): MapLayers {
    let mapLayers: List<MapLayer> = List();
    mapLayers = mapLayers.push(this.mapLayerService.osmLayer());
    mapLayers = mapLayers.push(this.mapLayerService.tileNameLayer());
    mapLayers = mapLayers.push(this.poiTileLayerService.buildLayer());
    mapLayers = mapLayers.push(this.mapLayerService.mainMapLayer());
    return new MapLayers(mapLayers);
  }

}
