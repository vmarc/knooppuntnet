import {OnInit} from "@angular/core";
import {ChangeDetectionStrategy, Component} from "@angular/core";
import {MatDialog} from "@angular/material/dialog";
import {Observable} from "rxjs";
import {combineLatest} from "rxjs";
import {tap} from "rxjs/operators";
import {map} from "rxjs/operators";
import {ZoomLevel} from "../../components/ol/domain/zoom-level";
import {MapZoomService} from "../../components/ol/services/map-zoom.service";
import {WarningDialogComponent} from "../../components/shared/dialog/warning-dialog.component";
import {PlannerService} from "../planner.service";
import {PlanPhase} from "../planner/plan/plan-phase";

/* tslint:disable:template-i18n English only */
@Component({
  selector: "kpn-plan-tip",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="planPhase$ | async as planPhase">
      <p *ngIf="planPhase === planPhaseEnum.zoomIn">
        Zoom in or use magnifying glass to find the startnode of your route.
      </p>
      <p *ngIf="planPhase === planPhaseEnum.clickStartNode">
        Use zoom, pan or use magnifying glass to find and click the
        startnode of your route.
      </p>
      <p *ngIf="planPhase === planPhaseEnum.clickEndNode">
        Click the endnode of your route.
      </p>
      <p *ngIf="planPhase === planPhaseEnum.extendRoute">
        Extend or adapt your route (<a (click)="more()">read more</a>). Output when satisfied.
      </p>
    </div>
  `,
  styles: [`
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

    const zoomLevel$ = this.mapZoomService.zoomLevel$.pipe(tap(() => console.log("Zoomlevel changed")));
    const plan$ = this.plannerService.context.planObserver.pipe(tap(() => console.log("plan changed")));

    this.planPhase$ = combineLatest([zoomLevel$, plan$]).pipe(
      map(([zoomLevel, plan]) => {
        let planPhase: PlanPhase = PlanPhase.zoomIn;
        if (zoomLevel >= ZoomLevel.vectorTileMinZoom) {
          if (plan.source == null) {
            planPhase = PlanPhase.clickStartNode;
          } else if (plan.legs.isEmpty()) {
            planPhase = PlanPhase.clickEndNode;
          } else {
            planPhase = PlanPhase.extendRoute;
          }
        }

        console.log("plan phase updated to " + planPhase + ", zoomLevel=" + zoomLevel);

        return planPhase;
      })
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
}
