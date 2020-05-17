import {OnDestroy} from "@angular/core";
import {ChangeDetectionStrategy, Component, OnInit} from "@angular/core";
import {Observable} from "rxjs";
import {shareReplay} from "rxjs/operators";
import {map} from "rxjs/operators";
import {PdfService} from "../../pdf/pdf.service";
import {GpxWriter} from "../../pdf/plan/gpx-writer";
import {Subscriptions} from "../../util/Subscriptions";
import {PlannerService} from "../planner.service";

/* tslint:disable:template-i18n English only */
@Component({
  selector: "kpn-plan-step-output",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button mat-stroked-button (click)="pdf1()" [disabled]="exportDisabled$ | async" i18n="@@planner.undo">Print (pdf)</button>
    <button mat-stroked-button (click)="pdf2()" [disabled]="exportDisabled$ | async" i18n="@@planner.redo">Node strip (pdf)</button>
    <button mat-stroked-button (click)="pdf3()" [disabled]="exportDisabled$ | async" i18n="@@planner.export">Navigation instructions (pdf)</button>
    <button mat-stroked-button (click)="gpx()" [disabled]="exportDisabled$ | async" i18n="@@planner.export">GPX file</button>
  `,
  styles: [`
    button {
      width: 100%;
      margin-bottom: 5px;
    }
  `]
})
export class PlanStepOutputComponent implements OnInit, OnDestroy {

  exportDisabled$: Observable<boolean>;

  private readonly subscriptions = new Subscriptions();

  constructor(private pdfService: PdfService,
              private plannerService: PlannerService) {
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

  pdf1() {
    this.pdfService.printDocument(this.plan());
  }

  pdf2() {
    this.pdfService.printStripDocument(this.plan());
  }

  pdf3() {
    this.pdfService.printInstructions(this.plan());
  }

  gpx() {
    new GpxWriter().write(this.plan());
  }

  private plan() {
    return this.plannerService.context.plan;
  }

}
