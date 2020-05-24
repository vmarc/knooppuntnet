import {OnInit} from "@angular/core";
import {ChangeDetectionStrategy, Component} from "@angular/core";
import {MatDialog} from "@angular/material/dialog";
import {Observable} from "rxjs";
import {combineLatest} from "rxjs";
import {delay} from "rxjs/operators";
import {map} from "rxjs/operators";
import {ZoomLevel} from "../../components/ol/domain/zoom-level";
import {MapZoomService} from "../../components/ol/services/map-zoom.service";
import {WarningDialogComponent} from "../../components/shared/dialog/warning-dialog.component";
import {PlannerService} from "../planner.service";
import {Plan} from "../planner/plan/plan";
import {PlanPhase} from "../planner/plan/plan-phase";

@Component({
  selector: "kpn-plan-tip",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="planPhase$ | async as planPhase" class="tip">
      <p *ngIf="planPhaseEnum.zoomInClickStartNode === planPhase" i18n="@@planner-tip.zoom-in-click-start-node">
        <b>Zoom in</b> or use magnifying glass to find the startnode of your route.
      </p>
      <p *ngIf="planPhaseEnum.clickStartNode === planPhase" i18n="@@planner-tip.click-start-node">
        Use zoom, pan or use magnifying glass to find and click the
        startnode of your route.
      </p>
      <p *ngIf="planPhaseEnum.zoomInClickEndNode === planPhase" i18n="@@planner-tip.zoom-in-click-end-node">
        <b>Zoom in</b>  and click the endnode of your route.
      </p>
      <p *ngIf="planPhaseEnum.clickEndNode === planPhase" i18n="@@planner-tip.click-end-node">
        Click the endnode of your route.
      </p>
      <p *ngIf="planPhaseEnum.extendRoute === planPhase" i18n="@@planner-tip.extend-route">
        Extend or adapt your route (<a (click)="more()">read more</a>). Output when satisfied.
      </p>
    </div>
  `,
  styles: [`
    .tip {
      height: 70px;
      font-style: italic;
    }
  `]
})
export class PlanTipComponent implements OnInit {

  planPhase$: Observable<PlanPhase>;
  planPhaseEnum = PlanPhase;

  constructor(private plannerService: PlannerService,
              private mapZoomService: MapZoomService,
              private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.planPhase$ = combineLatest([this.mapZoomService.zoomLevel$, this.plannerService.context.planObserver]).pipe(
      map(([zoomLevel, plan]) => this.determinePlanPhase(zoomLevel, plan)),
      delay(0)
    );
  }

  more(): void {

    let message = "This will be a link to that part of the documentation ";
    message += "that explains all possible interaction with the map while ";
    message += "planning a route. ";
    message += "This link has not been implemented yet.";

    this.dialog.open(
      WarningDialogComponent,
      {
        width: "450px",
        data: {
          title: "Link - not implemented yet",
          message: message
        }
      }
    );
  }

  private determinePlanPhase(zoomLevel: number, plan: Plan): PlanPhase {
    let planPhase: PlanPhase;
    if (plan.source === null) {
      if (zoomLevel < ZoomLevel.vectorTileMinZoom) {
        planPhase = PlanPhase.zoomInClickStartNode;
      } else {
        planPhase = PlanPhase.clickStartNode;
      }
    } else if (plan.legs.isEmpty()) {
      if (zoomLevel < ZoomLevel.vectorTileMinZoom) {
        planPhase = PlanPhase.zoomInClickEndNode;
      } else {
        planPhase = PlanPhase.clickEndNode;
      }
    } else {
      planPhase = PlanPhase.extendRoute;
    }
    return planPhase;
  }
}
