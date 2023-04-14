import { HttpErrorResponse } from '@angular/common/http';
import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PlanParams } from '@api/common/planner';
import { AppService } from '@app/app.service';
import { LegHttpErrorDialogComponent } from '@app/components/ol/components';
import { LegNotFoundDialogComponent } from '@app/components/ol/components';
import { NoRouteDialogComponent } from '@app/components/ol/components';
import { MapZoomService } from '@app/components/ol/services';
import { MapService } from '@app/components/ol/services';
import { NewMapService } from '@app/components/ol/services';
import { MAP_SERVICE_TOKEN } from '@app/components/ol/services';
import { PoiTileLayerService } from '@app/components/ol/services';
import { PageService } from '@app/components/shared';
import { Util } from '@app/components/shared';
import { selectFragment } from '@app/core';
import { selectQueryParam } from '@app/core';
import { PoiService } from '@app/services';
import { Subscriptions } from '@app/util';
import { Store } from '@ngrx/store';
import { Coordinate } from 'ol/coordinate';
import { combineLatest } from 'rxjs';
import { PlannerCommandAddPlan } from '../../domain/commands/planner-command-add-plan';
import { PlanBuilder } from '../../domain/plan/plan-builder';
import { PlannerService } from '../../planner.service';
import { actionPlannerMapViewInit } from '../../store/planner-actions';
import { actionPlannerInit } from '../../store/planner-actions';
import { selectPlannerNetworkType } from '../../store/planner-selectors';
import { PlannerMapService } from './planner-map.service';

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
  private planLoaded = false;
  private readonly subscriptions = new Subscriptions();

  constructor(
    protected service: PlannerMapService,
    private newMapService: NewMapService,
    private pageService: PageService,
    private mapService: MapService,
    private poiService: PoiService,
    private poiTileLayerService: PoiTileLayerService,
    private plannerService: PlannerService,
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
