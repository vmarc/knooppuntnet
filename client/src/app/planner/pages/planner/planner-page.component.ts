import { HttpErrorResponse } from '@angular/common/http';
import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PlanParams } from '@api/common/planner/plan-params';
import { AppService } from '@app/app.service';
import { LegHttpErrorDialogComponent } from '@app/components/ol/components/leg-http-error.dialog';
import { LegNotFoundDialogComponent } from '@app/components/ol/components/leg-not-found-dialog';
import { NoRouteDialogComponent } from '@app/components/ol/components/no-route-dialog.component';
import { MapLayerService } from '@app/components/ol/services/map-layer.service';
import { MapZoomService } from '@app/components/ol/services/map-zoom.service';
import { MapService } from '@app/components/ol/services/map.service';
import { PoiTileLayerService } from '@app/components/ol/services/poi-tile-layer.service';
import { PageService } from '@app/components/shared/page.service';
import { Util } from '@app/components/shared/util';
import { selectFragment } from '@app/core/core.state';
import { selectQueryParam } from '@app/core/core.state';
import { PlannerCommandAddPlan } from '@app/planner/domain/commands/planner-command-add-plan';
import { PlanBuilder } from '@app/planner/domain/plan/plan-builder';
import { PlannerPositionService } from '@app/planner/services/planner-position.service';
import { actionPlannerMapViewInit } from '@app/planner/store/planner-actions';
import { actionPlannerInit } from '@app/planner/store/planner-actions';
import { selectPlannerNetworkType } from '@app/planner/store/planner-selectors';
import { selectPlannerLayerStates } from '@app/planner/store/planner-selectors';
import { PoiService } from '@app/services/poi.service';
import { Subscriptions } from '@app/util/Subscriptions';
import { Store } from '@ngrx/store';
import { Coordinate } from 'ol/coordinate';
import { combineLatest } from 'rxjs';
import { PlannerService } from '../../services/planner.service';
import { NewMapService } from '@app/components/ol/services/new-map.service';
import { PlannerMapService } from '@app/planner/pages/planner/planner-map.service';
import { MAP_SERVICE_TOKEN } from '@app/components/ol/services/openlayers-map-service';

@Component({
  selector: 'kpn-planner-page',
  // TODO changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-planner-popup></kpn-planner-popup>
    <div [id]="service.mapId" class="map" (mouseleave)="mouseleave()">
      <kpn-route-control (action)="zoomInToRoute()" />
      <kpn-geolocation-control (action)="geolocation($event)" />
      <kpn-layer-switcher>
        <kpn-poi-menu />
      </kpn-layer-switcher>
      <kpn-map-link-menu />
    </div>
  `,
  styles: [
    `
      .map {
        position: absolute;
        top: 48px;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: white;
        overflow: hidden;
      }

      .map:-webkit-full-screen {
        top: 0;
      }
    `,
  ],
  providers: [
    {
      provide: MAP_SERVICE_TOKEN,
      useExisting: PlannerMapService,
    },
  ],
})
export class PlannerPageComponent implements OnInit, OnDestroy, AfterViewInit {
  readonly layerStates$ = this.store.select(selectPlannerLayerStates);

  private planLoaded = false;
  private readonly subscriptions = new Subscriptions();

  constructor(
    protected service: PlannerMapService,
    private newMapService: NewMapService,
    private pageService: PageService,
    private mapService: MapService,
    private mapLayerService: MapLayerService,
    private poiService: PoiService,
    private poiTileLayerService: PoiTileLayerService,
    private plannerService: PlannerService,
    private positionService: PlannerPositionService,
    private mapZoomService: MapZoomService,
    private dialog: MatDialog,
    private appService: AppService,
    private store: Store
  ) {
    this.store.dispatch(actionPlannerInit());
    this.plannerService.context.error$.subscribe((error) => {
      if (error instanceof HttpErrorResponse) {
        this.dialog.open(LegHttpErrorDialogComponent, {
          autoFocus: false,
          maxWidth: 600,
        });
      } else if ('leg-not-found' === error.message) {
        this.dialog.open(LegNotFoundDialogComponent, {
          autoFocus: false,
          maxWidth: 600,
        });
      }
    });
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.store.select(selectPlannerNetworkType).subscribe((networkType) => {
        this.pageService.nextToolbarBackgroundColor(
          'toolbar-style-' + networkType
        );
      })
    );
    this.subscriptions.add(
      combineLatest([
        this.store.select(selectPlannerNetworkType),
        this.store.select(selectQueryParam('plan')),
        this.store.select(selectFragment),
      ]).subscribe(([networkType, planQueryParam, fragment]) => {
        let planString: string = null;
        if (fragment) {
          // support old QR-codes
          planString = fragment;
        } else {
          planString = planQueryParam;
        }
        if (planString) {
          const planParams: PlanParams = {
            networkType,
            planString,
          };
          this.appService.plan(planParams).subscribe((response) => {
            const plan = PlanBuilder.build(response.result, planString);
            const command = new PlannerCommandAddPlan(plan);
            this.plannerService.context.execute(command);
            this.planLoaded = true;
            if (this.service.map) {
              this.zoomInToRoute();
            }
          });
        }
      })
    );
  }

  mouseleave() {
    this.plannerService.engine.handleMouseLeave();
  }

  ngAfterViewInit(): void {
    this.store.dispatch(actionPlannerMapViewInit());
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    this.pageService.nextToolbarBackgroundColor(null);
    this.plannerService.context.destroy();
    this.service.destroy();
  }

  zoomInToRoute(): void {
    if (this.plannerService.context.plan.legs.isEmpty()) {
      this.dialog.open(NoRouteDialogComponent, {
        autoFocus: false,
        maxWidth: 600,
      });
    } else {
      const bounds = this.plannerService.context.plan.bounds();
      if (bounds !== null) {
        const extent = Util.toExtent(bounds, 0.1);
        this.service.map.getView().fit(extent);
      }
    }
  }

  geolocation(coordinate: Coordinate): void {
    this.service.map.getView().setCenter(coordinate);
    let zoomLevel = 15;
    if ('cycling' === this.mapService.networkType()) {
      zoomLevel = 13;
    }
    this.service.map.getView().setZoom(zoomLevel);
  }
}
