import {AfterViewInit, Component, OnDestroy, OnInit} from "@angular/core";
import {MatDialog} from "@angular/material/dialog";
import {ActivatedRoute} from "@angular/router";
import {List} from "immutable";
import Map from "ol/Map";
import Overlay from "ol/Overlay";
import View from "ol/View";
import {NoRouteDialogComponent} from "../../../components/ol/components/no-route-dialog.component";
import {MapGeocoder} from "../../../components/ol/domain/map-geocoder";
import {ZoomLevel} from "../../../components/ol/domain/zoom-level";
import {MapControls} from "../../../components/ol/layers/map-controls";
import {MapLayer} from "../../../components/ol/layers/map-layer";
import {MapLayers} from "../../../components/ol/layers/map-layers";
import {MapLayerService} from "../../../components/ol/services/map-layer.service";
import {MapPositionService} from "../../../components/ol/services/map-position.service";
import {MapZoomService} from "../../../components/ol/services/map-zoom.service";
import {MapService} from "../../../components/ol/services/map.service";
import {PoiTileLayerService} from "../../../components/ol/services/poi-tile-layer.service";
import {PageService} from "../../../components/shared/page.service";
import {Util} from "../../../components/shared/util";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {PoiService} from "../../../services/poi.service";
import {Subscriptions} from "../../../util/Subscriptions";
import {PlannerService} from "../../planner.service";
import {PlannerInteraction} from "../../planner/interaction/planner-interaction";

@Component({
  selector: "kpn-map-main-page",
  // TODO changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-map-popup></kpn-map-popup>
    <div id="main-map" class="map">
      <kpn-route-control (action)="zoomInToRoute()"></kpn-route-control>
      <kpn-layer-switcher [mapLayers]="layers">
        <kpn-poi-menu></kpn-poi-menu>
      </kpn-layer-switcher>
    </div>
  `,
  styles: [`
    .map {
      position: absolute;
      top: 48px;
      left: 0;
      right: 0;
      bottom: 0;
      background-color: white;
    }
  `]
})
export class MapMainPageComponent implements OnInit, OnDestroy, AfterViewInit {

  layers: MapLayers;
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
              private mapPositionService: MapPositionService,
              private mapZoomService: MapZoomService,
              private dialog: MatDialog) {
    this.pageService.showFooter = false;
  }

  ngOnInit(): void {

    this.subscriptions.add(
      this.activatedRoute.params.subscribe(params => {
        const networkTypeName = params["networkType"];
        const networkType = NetworkType.withName(networkTypeName);
        this.mapService.networkType$.next(networkType);
        this.layers = this.buildLayers();
      })
    );

    this.subscriptions.add(
      this.pageService.sidebarOpen.subscribe(state => {
        if (this.map) {
          setTimeout(() => this.map.updateSize(), 250);
        }
      })
    );
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
    this.plannerService.context.setNetworkType(this.mapService.networkType$.value);
    this.interaction.addToMap(this.map);

    const view = this.map.getView();
    this.mapPositionService.install(view);
    this.poiService.updateZoomLevel(view.getZoom());
    this.mapZoomService.install(view);

    MapGeocoder.install(this.map);
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
    this.subscriptions.unsubscribe();
  }

  zoomInToRoute(): void {
    if (this.plannerService.context.plan.legs.isEmpty()) {
      this.dialog.open(NoRouteDialogComponent, {maxWidth: 600});
    } else {
      const bounds = this.plannerService.context.plan.bounds();
      if (bounds !== null) {
        const extent = Util.toExtent(bounds, 0.1);
        this.map.getView().fit(extent);
      }
    }
  }

  private buildLayers(): MapLayers {
    let mapLayers: List<MapLayer> = List();
    mapLayers = mapLayers.push(this.mapLayerService.osmLayer());
    // mapLayers = mapLayers.push(this.mapLayerService.tileNameLayer());
    mapLayers = mapLayers.push(this.poiTileLayerService.buildLayer());
    mapLayers = mapLayers.push(this.mapLayerService.mainMapLayer());
    return new MapLayers(mapLayers);
  }
}
