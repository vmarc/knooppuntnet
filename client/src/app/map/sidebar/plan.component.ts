import {Component, OnDestroy, OnInit} from "@angular/core";
import {MatDialog} from "@angular/material";
import {PdfService} from "../../pdf/pdf.service";
import {GpxWriter} from "../../pdf/plan/gpx-writer";
import {Subscriptions} from "../../util/Subscriptions";
import {PlannerService} from "../planner.service";
import {Plan} from "../planner/plan/plan";
import {ExportDialogComponent} from "./export-dialog.component";

@Component({
  selector: "kpn-plan",
  template: `

    <kpn-plan-translations></kpn-plan-translations>
    <kpn-poi-names></kpn-poi-names>

    <div class="buttons">
      <button mat-stroked-button (click)="undo()" [disabled]="!undoEnabled()" i18n="@@planner.undo">Undo</button>
      <button mat-stroked-button (click)="redo()" [disabled]="!redoEnabled()" i18n="@@planner.redo">Redo</button>
      <button mat-stroked-button (click)="export()" [disabled]="!exportEnabled()" i18n="@@planner.export">Export</button>
    </div>

    <div class="menu">
      <span>
        <a [ngClass]="{'selected': mode === 'compact'}" (click)="mode = 'compact'" i18n="@@planner.compact">
          Compact
        </a>
      </span>
      <span>
        <a [ngClass]="{'selected': mode === 'detailed'}" (click)="mode = 'detailed'" i18n="@@planner.detailed">
          Detailed
        </a>
      </span>
      <span>
        <a [ngClass]="{'selected': mode === 'instructions'}" (click)="mode = 'instructions'" i18n="@@planner.instructions">
          Instructions
        </a>
      </span>
    </div>

    <kpn-plan-distance [plan]="plan"></kpn-plan-distance>
    <kpn-plan-compact *ngIf="mode == 'compact'" [plan]="plan"></kpn-plan-compact>
    <kpn-plan-detailed *ngIf="mode == 'detailed'" [plan]="plan"></kpn-plan-detailed>
    <kpn-plan-instructions *ngIf="mode == 'instructions'" [plan]="plan"></kpn-plan-instructions>

  `,
  styles: [`

    .buttons {
      padding-top: 15px;
      padding-bottom: 15px;
    }

    .buttons :not(:last-child) {
      margin-right: 10px;
    }

    .menu {
      padding-bottom: 5px;
    }

    .menu :not(:last-child):after {
      content: " | ";
      padding-left: 5px;
      padding-right: 5px;
    }

    a.selected {
      color: rgba(0, 0, 0, 0.87);
      font-weight: bold;
    }

    a:hover {
      cursor: pointer;
    }

  `],
  providers: [
    PdfService
  ]
})
export class PlanComponent implements OnInit, OnDestroy {

  plan: Plan;
  mode = "compact"; // compact | detailed | instructions
  private readonly subscriptions = new Subscriptions();

  constructor(private plannerService: PlannerService,
              private pdfService: PdfService,
              private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.subscriptions.add(this.plannerService.context.planObserver.subscribe(plan => this.plan = plan));
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  undo() {
    this.plannerService.context.undo();
  }

  redo() {
    this.plannerService.context.redo();
  }

  undoEnabled(): boolean {
    return this.plannerService.context.commandStack.canUndo;
  }

  redoEnabled(): boolean {
    return this.plannerService.context.commandStack.canRedo;
  }

  exportEnabled(): boolean {
    return !this.plannerService.context.plan.legs.isEmpty();
  }

  export(): void {
    const dialogRef = this.dialog.open(ExportDialogComponent);
    dialogRef.afterClosed().subscribe(result => {
      if (result == "pdf1") {
        this.pdfService.printDocument(this.plan);
      } else if (result == "pdf2") {
        this.pdfService.printStripDocument(this.plan);
      } else if (result == "pdf3") {
        this.pdfService.printInstructions(this.plan);
      } else if (result == "gpx") {
        new GpxWriter().write(this.plan);
      }
    });
  }

}
