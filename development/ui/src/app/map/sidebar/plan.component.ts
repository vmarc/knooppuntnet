import {Component, OnDestroy, OnInit} from "@angular/core";
import {MatDialog} from "@angular/material";
import {Subscription} from "rxjs";
import {PdfService} from "../../pdf/pdf.service";
import {PlannerService} from "../planner.service";
import {Plan} from "../planner/plan/plan";
import {ExportDialogComponent} from "./export-dialog.component";

@Component({
  selector: "kpn-plan",
  template: `

    <kpn-plan-translations></kpn-plan-translations>
    
    <div class="buttons">
      <button mat-raised-button (click)="undo()" [disabled]="!undoEnabled()">Undo</button>
      <button mat-raised-button (click)="redo()" [disabled]="!redoEnabled()">Redo</button>
      <button mat-raised-button (click)="export()" [disabled]="!exportEnabled()">Export</button>
    </div>

    <div class="menu">
      <span>
        <a [ngClass]="{'selected': mode === 'compact'}" (click)="mode = 'compact'">
          Compact
        </a>
      </span>
      <span>
        <a [ngClass]="{'selected': mode === 'detailed'}" (click)="mode = 'detailed'">
          Detailed
        </a>
      </span>
      <span>
        <a [ngClass]="{'selected': mode === 'instructions'}" (click)="mode = 'instructions'">
          Instructions
        </a>
      </span>
    </div>

    <kpn-plan-compact *ngIf="mode == 'compact'" [plan]="plan"></kpn-plan-compact>
    <kpn-plan-detailed *ngIf="mode == 'detailed'" [plan]="plan"></kpn-plan-detailed>
    <kpn-plan-instructions *ngIf="mode == 'instructions'" [plan]="plan"></kpn-plan-instructions>

  `,
  styles: [`

    .buttons {
      padding-top: 15px;
      padding-bottom: 15px;
    }

    .undo-redo-buttons :not(:last-child) {
      margin-right: 10px;
    }

    .menu {
      padding-bottom: 15px;
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

  `],
  providers: [
    PdfService
  ]
})
export class PlanComponent implements OnInit, OnDestroy {

  plan: Plan;
  planSubscription: Subscription;

  mode = "compact"; // compact | detailed | instructions

  constructor(private plannerService: PlannerService,
              private pdfService: PdfService,
              private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.planSubscription = this.plannerService.context.planObserver.subscribe(plan => this.plan = plan);
  }

  ngOnDestroy(): void {
    if (this.planSubscription) {
      this.planSubscription.unsubscribe();
    }
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
        this.pdfService.printHorizontal(this.plan);
      } else if (result == "pdf2") {
        this.pdfService.printVertical(this.plan);
      } else if (result == "pdf3") {
        this.pdfService.printInstructions(this.plan);
      }
    });
  }

}
