import {ChangeDetectionStrategy, Component, OnInit} from "@angular/core";
import {MatDialog} from "@angular/material/dialog";
import {Observable} from "rxjs";
import {PlannerService} from "../planner.service";
import {PlannerCommandReset} from "../planner/commands/planner-command-reset";
import {Plan} from "../planner/plan/plan";

@Component({
  selector: "kpn-plan-actions",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="buttons" *ngIf="plan$ | async">
      <button
        mat-stroked-button
        (click)="undo()"
        [disabled]="!undoEnabled()"
        title="undo the previous action"
        i18n="@@planner.undo">
        Undo
      </button>

      <button
        mat-stroked-button
        (click)="redo()"
        [disabled]="!redoEnabled()"
        title="redo the action that was previously undone"
        i18n="@@planner.redo">
        Redo
      </button>

      <button
        mat-stroked-button
        (click)="restart()"
        [disabled]="!restartEnabled()"
        title="Wipe out current route plan and restart route planning from scratch"
        i18n="@@planner.reset">
        Reset
      </button>

    </div>
  `,
  styles: [`
    .buttons {
      padding-top: 15px;
      padding-bottom: 15px;
    }

    .buttons :not(:last-child) {
      margin-right: 10px;
    }
  `]
})
export class PlanActionsComponent implements OnInit {

  plan$: Observable<Plan>;

  constructor(private plannerService: PlannerService,
              private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.plan$ = this.plannerService.context.planObserver;
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

  restartEnabled(): boolean {
    return this.plannerService.context.commandStack.canUndo;
  }

  undoEnabled(): boolean {
    return this.plannerService.context.commandStack.canUndo;
  }

  redoEnabled(): boolean {
    return this.plannerService.context.commandStack.canRedo;
  }
}
