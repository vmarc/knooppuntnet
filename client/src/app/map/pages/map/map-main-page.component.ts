import {AfterViewInit, Component, OnDestroy, OnInit} from "@angular/core";
import {MatDialog} from "@angular/material/dialog";
import {ActivatedRoute} from "@angular/router";
import {Coordinate} from "ol/coordinate";
import Map from "ol/Map";
import Overlay from "ol/Overlay";
import View from "ol/View";
import {Observable} from "rxjs";
import {combineLatest} from "rxjs";
import {delay} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {NoRouteDialogComponent} from "../../../components/ol/components/no-route-dialog.component";
import {MapGeocoder} from "../../../components/ol/domain/map-geocoder";
import {ZoomLevel} from "../../../components/ol/domain/zoom-level";
import {MapControls} from "../../../components/ol/layers/map-controls";
import {MapLayers} from "../../../components/ol/layers/map-layers";
import {MapLayerService} from "../../../components/ol/services/map-layer.service";
import {MapPositionService} from "../../../components/ol/services/map-position.service";
import {MapZoomService} from "../../../components/ol/services/map-zoom.service";
import {MapService} from "../../../components/ol/services/map.service";
import {PoiTileLayerService} from "../../../components/ol/services/poi-tile-layer.service";
import {PageService} from "../../../components/shared/page.service";
import {Util} from "../../../components/shared/util";
import {PlanParams} from "../../../kpn/api/common/planner/plan-params";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {PoiService} from "../../../services/poi.service";
import {Subscriptions} from "../../../util/Subscriptions";
import {PlannerService} from "../../planner.service";
import {PlannerInteraction} from "../../planner/interaction/planner-interaction";
import {Plan} from "../../planner/plan/plan";
import {PlanLegBuilder} from "../../planner/plan/plan-leg-builder";
import {PlannerLayerService} from "../../planner/services/planner-layer.service";

@Component({
  selector: "kpn-map-main-page",
  // TODO changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-map-popup></kpn-map-popup>
    <div id="main-map" class="map">
      <kpn-route-control (action)="zoomInToRoute()"></kpn-route-control>
      <kpn-geolocation-control (action)="geolocation($event)"></kpn-geolocation-control>
      <kpn-layer-switcher [mapLayers]="mapLayers$ | async">
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
      overflow: hidden;
    }
  `]
})
export class MapMainPageComponent implements OnInit, OnDestroy, AfterViewInit {

  mapLayers$: Observable<MapLayers>;
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
              private plannerLayerService: PlannerLayerService,
              private dialog: MatDialog,
              private appService: AppService) {
    this.pageService.showFooter = false;
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.activatedRoute.params.subscribe(params => {
        const networkTypeName = params["networkType"];
        const networkType = NetworkType.withName(networkTypeName);
        this.mapService.nextNetworkType(networkType);
        this.plannerService.context.nextNetworkType(networkType);
      })
    );

    this.subscriptions.add(
      this.mapService.networkType$.subscribe(networkType => {
        this.pageService.nextToolbarBackgroundColor("toolbar-style-" + networkType.name);
      })
    );

    this.subscriptions.add(
      combineLatest([this.plannerService.context.networkType$, this.activatedRoute.fragment])
        .subscribe(([networkType, fragment]) => {
          if (fragment) {
            const planParams = new PlanParams(networkType.name, fragment);
            this.appService.plan(planParams).subscribe(response => {
              const legs = response.result.map(routeLeg => PlanLegBuilder.toPlanLeg2(routeLeg));
              const plan = Plan.create(legs.get(0).sourceNode, legs);
              plan.legs.forEach(leg => this.plannerService.context.legs.add(leg));
              this.plannerService.context.routeLayer.addPlan(plan);
              this.plannerService.context.updatePlan(plan);
              this.zoomInToRoute();
            });
          }
        })
    );
  }

  ngAfterViewInit(): void {
    setTimeout(() => this.buildMap(), 1);
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
    this.subscriptions.unsubscribe();
    this.pageService.nextToolbarBackgroundColor(null);
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

  geolocation(coordinate: Coordinate): void {
    this.map.getView().setCenter(coordinate);
    let zoomLevel = 15;
    if (NetworkType.cycling === this.mapService.networkType()) {
      zoomLevel = 13;
    }
    this.map.getView().setZoom(zoomLevel);
  }

  private buildMap(): void {
    this.plannerLayerService.init();
    this.mapLayers$ = this.plannerLayerService.mapLayers$.pipe(
      delay(0)
    );

    this.overlay = new Overlay({
      id: "popup",
      element: document.getElementById("popup"),
      autoPan: true,
      autoPanAnimation: {
        duration: 250
      }
    });

    const layers = this.plannerLayerService.standardLayers.map(ml => ml.layer).toArray();

    this.map = new Map({
      target: "main-map",
      layers: layers,
      overlays: [this.overlay],
      controls: MapControls.build(),
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.vectorTileMaxOverZoom
      })
    });

    this.plannerLayerService.applyMap(this.map);

    this.plannerService.init(this.map);
    this.interaction.addToMap(this.map);

    const view = this.map.getView();
    this.mapPositionService.install(view);
    this.poiService.updateZoomLevel(view.getZoom());
    this.mapZoomService.install(view);

    MapGeocoder.install(this.map);

    this.subscriptions.add(
      this.pageService.sidebarOpen.subscribe(state => {
        if (this.map) {
          setTimeout(() => this.map.updateSize(), 250);
        }
      })
    );
  }
}
