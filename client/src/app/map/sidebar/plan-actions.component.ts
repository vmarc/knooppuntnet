import {ChangeDetectionStrategy, Component, OnInit} from "@angular/core";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {PageWidth} from "../../components/shared/page-width";
import {PageWidthService} from "../../components/shared/page-width.service";
import {PlannerService} from "../planner.service";
import {PlannerCommandReset} from "../planner/commands/planner-command-reset";
import {Plan} from "../planner/plan/plan";

@Component({
  selector: "kpn-plan-actions",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="buttons" *ngIf="plan$ | async as plan">
      <div *ngIf="showButtonText$ | async">

        <button
          mat-stroked-button
          (click)="undo()"
          [disabled]="!undoEnabled()"
          title="Undo the previous action"
          i18n-title="@@planner.action.undo.title">
          <mat-icon svgIcon="undo"></mat-icon>
          <span class="button-text" i18n="@@planner.action.undo">Undo</span>
        </button>

        <button
          mat-stroked-button
          (click)="redo()"
          [disabled]="!redoEnabled()"
          title="Redo the action that was previously undone"
          i18n-title="@@planner.action.redo.title">
          <mat-icon svgIcon="redo"></mat-icon>
          <span class="button-text" i18n="@@planner.action.redo">Redo</span>
        </button>

        <button
          mat-stroked-button
          (click)="restart()"
          [disabled]="!hasStartNode(plan)"
          title="Wipe out current route plan and restart route planning from scratch"
          i18n-title="@@planner.action.reset.title">
          <mat-icon svgIcon="reset"></mat-icon>
          <span class="button-text" i18n="@@planner.action.reset">Reset</span>
        </button>

        <button
          mat-stroked-button
          (click)="reverse()"
          [disabled]="!hasRoute(plan)"
          title="Reverse the route direction (startnode becomes endnode, and vice versa)"
          i18n-title="@@planner.action.reverse.title">
          <mat-icon svgIcon="reverse"></mat-icon>
          <span class="button-text" i18n="@@planner.action.reverse">Reverse</span>
        </button>

        <button
          mat-stroked-button
          (click)="output()"
          [disabled]="!hasRoute(plan)"
          title="Output planned route"
          i18n-title="@@planner.action.output.title">
          <mat-icon svgIcon="output"></mat-icon>
          <span class="button-text" i18n="@@planner.action.output">Output</span>
        </button>

      </div>

      <div *ngIf="showButtonIcon$ | async">

        <button
          mat-icon-button
          (click)="undo()"
          [disabled]="!undoEnabled()"
          title="Undo the previous action"
          i18n-title="@@planner.action.undo.title">
          <mat-icon svgIcon="undo"></mat-icon>
        </button>

        <button
          mat-icon-button
          (click)="redo()"
          [disabled]="!redoEnabled()"
          title="Redo the action that was previously undone"
          i18n-title="@@planner.action.redo.title">
          <mat-icon svgIcon="redo"></mat-icon>
        </button>

        <button
          mat-icon-button
          *ngIf="showRestartButton$ | async"
          (click)="restart()"
          [disabled]="!hasStartNode(plan)"
          title="Wipe out current route plan and restart route planning from scratch"
          i18n-title="@@planner.action.reset.title">
          <mat-icon svgIcon="reset"></mat-icon>
        </button>

        <button
          mat-icon-button
          *ngIf="showReverseButton$ | async"
          (click)="reverse()"
          [disabled]="!hasRoute(plan)"
          title="Reverse the route direction (startnode becomes endnode, and vice versa)"
          i18n-title="@@planner.action.reverse.title">
          <mat-icon svgIcon="reverse"></mat-icon>
        </button>

        <button
          mat-icon-button
          (click)="output()"
          [disabled]="!hasRoute(plan)"
          title="Output planned route"
          i18n-title="@@planner.action.output.title">
          <mat-icon svgIcon="output"></mat-icon>
        </button>

      </div>
    </div>
  `,
  styles: [`
    .buttons {
      display: inline-block;
      padding-top: 15px;
      padding-bottom: 15px;
    }
    .buttons button {
      margin-right: 10px;
    }
    .button-text {
      padding-left: 10px;
    }
  `]
})
export class PlanActionsComponent implements OnInit {

  plan$: Observable<Plan>;

  showButtonText$: Observable<boolean>;
  showButtonIcon$: Observable<boolean>;
  showRestartButton$: Observable<boolean>;
  showReverseButton$: Observable<boolean>;

  constructor(private plannerService: PlannerService,
              private pageWidthService: PageWidthService) {
  }

  ngOnInit(): void {
    this.plan$ = this.plannerService.context.planObserver;
    this.showButtonText$ = this.pageWidthService.current$.pipe(map(pageWidth => pageWidth === PageWidth.veryLarge || pageWidth === PageWidth.large));
    this.showButtonIcon$ = this.showButtonText$.pipe(map(enabled => !enabled));
    this.showRestartButton$ = this.pageWidthService.current$.pipe(map(pageWidth => pageWidth !== PageWidth.verySmall));
    this.showReverseButton$ = this.pageWidthService.current$.pipe(map(pageWidth => pageWidth !== PageWidth.verySmall));
  }

  undo(): void {
    this.plannerService.context.undo();
  }

  redo(): void {
    this.plannerService.context.redo();
  }

  restart(): void {
    const command = new PlannerCommandReset();
    this.plannerService.context.execute(command);
  }

  reverse(): void {
  }

  output(): void {
  }

  undoEnabled(): boolean {
    return this.plannerService.context.commandStack.canUndo;
  }

  redoEnabled(): boolean {
    return this.plannerService.context.commandStack.canRedo;
  }

  hasStartNode(plan: Plan): boolean {
    return plan.source !== null;
  }

  hasRoute(plan: Plan): boolean {
    return !plan.legs.isEmpty();
  }
}
