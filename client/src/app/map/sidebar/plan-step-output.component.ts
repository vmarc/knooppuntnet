import {OnDestroy} from "@angular/core";
import {ChangeDetectionStrategy, Component, OnInit} from "@angular/core";
import {MatDialog} from "@angular/material/dialog";
import {Observable} from "rxjs";
import {shareReplay} from "rxjs/operators";
import {map} from "rxjs/operators";
import {WarningDialogComponent} from "../../components/shared/dialog/warning-dialog.component";
import {PdfService} from "../../pdf/pdf.service";
import {GpxWriter} from "../../pdf/plan/gpx-writer";
import {Subscriptions} from "../../util/Subscriptions";
import {PlannerService} from "../planner.service";

/* tslint:disable:template-i18n English only */
@Component({
  selector: "kpn-plan-step-output",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="buttons">

      <button
        mat-stroked-button
        (click)="reverse()"
        [disabled]="exportDisabled$ | async"
        title="Reverse the route direction (startnode becomes endnode, and vice versa)"
        i18n-title="@@planner.output.reverse.title"
        i18n="@@planner.output.reverse">
        Reverse route
      </button>

      <div class="separator"></div>

      <button
        mat-stroked-button
        (click)="pdf1()"
        [disabled]="exportDisabled$ | async"
        title="Produce a route pdf file with compact node overview"
        i18n-title="@@planner.output.compact-pdf.title"
        i18n="@@planner.output.compact-pdf">
        Compact print
      </button>

      <button
        mat-stroked-button
        (click)="pdf2()"
        [disabled]="exportDisabled$ | async"
        title="Produce a route pdf file with nodes in 'strip' format"
        i18n-title="@@planner.output.node-strip-pdf.title"
        i18n="@@planner.output.node-strip-pdf">
        Node strip
      </button>

      <button
        mat-stroked-button
        (click)="pdf3()"
        [disabled]="exportDisabled$ | async"
        title="Produce a route pdf with navigation instructions"
        i18n-title="@@planner.output.navigation-instructions-pdf.title"
        i18n="@@planner.output.navigation-instructions-pdf">
        Navigation instructions
      </button>

      <button
        mat-stroked-button
        (click)="gpx()"
        [disabled]="exportDisabled$ | async"
        title="Produce a route file that can be used in a gps-device"
        i18n-title="@@planner.output.gpx.title"
        i18n="@@planner.output.gpx">
        GPX
      </button>
    </div>
  `,
  styles: [`
    .buttons {
      padding-top: 5px;
      padding-bottom: 5px;
      padding-left: 22px;
      border-left: 1px solid rgba(0, 0, 0, 0.12);
    }

    button {
      width: 100%;
      margin-bottom: 5px;
    }

    .separator {
      height: 15px;
    }
  `]
})
export class PlanStepOutputComponent implements OnInit, OnDestroy {

  exportDisabled$: Observable<boolean>;

  private readonly subscriptions = new Subscriptions();

  constructor(private pdfService: PdfService,
              private plannerService: PlannerService,
              private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.exportDisabled$ = this.plannerService.context.planObserver.pipe(
      map(plan => plan.legs.isEmpty()),
      shareReplay()
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  pdf1(): void {
    this.pdfService.printDocument(this.plan());
  }

  pdf2(): void {
    this.pdfService.printStripDocument(this.plan());
  }

  pdf3(): void {
    this.pdfService.printInstructions(this.plan());
  }

  gpx(): void {
    new GpxWriter().write(this.plan());
  }

  reverse(): void {

    let message = "This action will reverse the direction of your planned route. ";
    message += "The start-node will become the end-node, and vice versa. ";
    message += "This action has not been implemented yet.";

    this.dialog.open(
      WarningDialogComponent,
      {
        width: "450px",
        data: {
          title: "Reverse route - not implemented yet",
          message: message
        }
      }
    );
  }

  private plan() {
    return this.plannerService.context.plan;
  }
}
