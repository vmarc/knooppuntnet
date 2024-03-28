import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { effect } from '@angular/core';
import { inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PlanParams } from '@api/common/planner';
import { NetworkType } from '@api/custom';
import { Util } from '@app/components/shared';
import { PageService } from '@app/components/shared';
import { NoRouteDialogComponent } from '@app/ol/components';
import { LegNotFoundDialogComponent } from '@app/ol/components';
import { LegHttpErrorDialogComponent } from '@app/ol/components';
import { MapMode } from '@app/ol/services';
import { ApiService } from '@app/services';
import { Subscriptions } from '@app/util';
import { Coordinate } from 'ol/coordinate';
import { SharedStateService } from '../../../shared/core/shared/shared-state.service';
import { RouterService } from '../../../shared/services/router.service';
import { PlannerCommandAddPlan } from '../../domain/commands/planner-command-add-plan';
import { PlanBuilder } from '../../domain/plan/plan-builder';
import { PlanUtil } from '../../domain/plan/plan-util';
import { PlannerMapService } from './planner-map.service';
import { PlannerStateService } from './planner-state.service';
import { PlannerService } from './planner.service';

@Injectable()
export class PlannerPageService {
  private readonly plannerStateService = inject(PlannerStateService);
  private readonly plannerService = inject(PlannerService);
  private readonly plannerMapService = inject(PlannerMapService);
  private readonly pageService = inject(PageService);
  private readonly dialog = inject(MatDialog);
  private readonly apiService = inject(ApiService);
  private readonly sharedStateService = inject(SharedStateService);
  private readonly routerService = inject(RouterService);

  private readonly subscriptions = new Subscriptions();

  readonly mapId = this.plannerMapService.mapId;
  readonly networkType = this.plannerStateService.networkType;

  constructor() {
    this.plannerStateService.onInit();
    this.setNetworkType(this.plannerStateService.networkType());
    this.sharedStateService.loadSurveyDateValues();
    effect(() => {
      const error = this.plannerService.context.error();
      if (error) {
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
      }
    });
  }

  onInit(): void {
    this.pageService.setToolbarBackgroundColor(
      'toolbar-style-' + this.plannerStateService.networkType()
    );

    const networkType = this.plannerStateService.networkType();
    const planString = this.routerService.queryParam('plan');
    if (planString) {
      const planParams: PlanParams = {
        networkType,
        planString,
      };
      this.apiService.plan(planParams).subscribe((response) => {
        const plan = PlanBuilder.build(response.result, planString);
        const command = new PlannerCommandAddPlan(plan);
        this.plannerService.context.execute(command);
        if (this.plannerMapService.map) {
          this.zoomInToRoute();
        }
      });
    }
  }

  setNetworkType(networkType: NetworkType): void {
    this.plannerStateService.setNetworkType(networkType);
    this.plannerService.context.setNetworkType(networkType);
    this.plannerMapService.setNetworkType(networkType);
  }

  setMapMode(mapMode: MapMode): void {
    this.plannerStateService.setMapMode(mapMode);
    this.plannerMapService.setMapMode(mapMode);
  }

  mouseleave() {
    this.plannerService.engine.handleMouseLeave();
  }

  afterViewInit(): void {
    this.plannerMapService.init(this.plannerStateService.state());
  }

  onDestroy(): void {
    this.subscriptions.unsubscribe();
    this.pageService.setToolbarBackgroundColor(null);
    this.plannerService.context.destroy();
    this.plannerMapService.destroy();
  }

  zoomInToRoute(): void {
    if (this.plannerService.context.plan().legs.isEmpty()) {
      this.dialog.open(NoRouteDialogComponent, {
        autoFocus: false,
        maxWidth: 600,
      });
    } else {
      const bounds = PlanUtil.planBounds(this.plannerService.context.plan());
      if (bounds !== null) {
        const extent = Util.toExtent(bounds, 0.1);
        this.plannerMapService.map.getView().fit(extent);
      }
    }
  }

  geolocation(coordinate: Coordinate): void {
    this.plannerMapService.map.getView().setCenter(coordinate);
    let zoomLevel = 15;
    if ('cycling' === this.plannerStateService.networkType()) {
      zoomLevel = 13;
    }
    this.plannerMapService.map.getView().setZoom(zoomLevel);
  }
}
