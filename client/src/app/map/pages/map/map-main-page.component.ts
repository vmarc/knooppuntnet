import {AfterViewInit, Component, OnDestroy, OnInit} from "@angular/core";
import {MatDialog} from "@angular/material/dialog";
import {ActivatedRoute} from "@angular/router";
import {List} from "immutable";
import {Coordinate} from "ol/coordinate";
import Map from "ol/Map";
import Overlay from "ol/Overlay";
import View from "ol/View";
import {Observable} from "rxjs";
import {combineLatest} from "rxjs";
import {delay, map} from "rxjs/operators";
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
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {PoiService} from "../../../services/poi.service";
import {Subscriptions} from "../../../util/Subscriptions";
import {PlannerService} from "../../planner.service";
import {FeatureId} from "../../planner/features/feature-id";
import {PlannerInteraction} from "../../planner/interaction/planner-interaction";
import {Plan} from "../../planner/plan/plan";
import {PlanLeg} from "../../planner/plan/plan-leg";
import {PlanLegBuilder} from "../../planner/plan/plan-leg-builder";
import {PlanLegNodeIds} from "../../planner/plan/plan-leg-node-ids";
import {PlanRoute} from "../../planner/plan/plan-route";
import {PlanUtil} from "../../planner/plan/plan-util";
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
            const nodeIds = PlanUtil.toNodeIds(fragment);
            let legNodeIdss: List<PlanLegNodeIds> = List();
            for (let i = 0; i < nodeIds.size - 1; i++) {
              legNodeIdss = legNodeIdss.push(new PlanLegNodeIds(nodeIds.get(i), nodeIds.get(i + 1)));
            }

            const legObservables = legNodeIdss.map(legNodeIds => {
              return this.appService.routeLeg(networkType.name, FeatureId.next(), legNodeIds.sourceNodeId, legNodeIds.sinkNodeId).pipe(
                map(response => PlanLegBuilder.toPlanLeg2(response.result))
              );
            }).toArray();

            const legsObservable = combineLatest(legObservables);
            legsObservable.subscribe(legs => {
              for (let i = 1; i < legs.length; i++) {
                let newRoutes: List<PlanRoute> = List();
                for (let j = 0; j < legs[i].routes.size; j++) {
                  const oldRoute = legs[i].routes.get(j);
                  const newSource = j === 0 ? legs[i - 1].sink : oldRoute.source;
                  newRoutes = newRoutes.push(
                    new PlanRoute(
                      newSource,
                      oldRoute.sink,
                      oldRoute.meters,
                      oldRoute.segments,
                      oldRoute.streets
                    )
                  );
                }

                legs[i] = new PlanLeg(
                  legs[i].featureId,
                  legs[i - 1].sink,
                  legs[i].sink,
                  legs[i].meters,
                  newRoutes
                );
              }
              legs.forEach(leg => this.plannerService.context.legs.add(leg));
              const plan = Plan.create(legs[0].source, List(legs));
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

    this.map = new Map({
      target: "main-map",
      layers: this.plannerLayerService.standardLayers.map(ml => ml.layer).toArray(),
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
