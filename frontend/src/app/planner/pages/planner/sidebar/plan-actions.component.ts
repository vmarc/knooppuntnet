import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { Injector } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PageWidth } from '@app/components/shared';
import { PageWidthService } from '@app/components/shared';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { PlannerCommandReset } from '../../../domain/commands/planner-command-reset';
import { PlannerCommandReverse } from '../../../domain/commands/planner-command-reverse';
import { Plan } from '../../../domain/plan/plan';
import { PlanReverser } from '../../../domain/plan/plan-reverser';
import { PlannerService } from '../../../planner.service';
import { PlanActionButtonComponent } from './plan-action-button.component';
import { PlanOutputDialogComponent } from './plan-output-dialog.component';

@Component({
  selector: 'kpn-plan-actions',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="buttons" *ngIf="plan$ | async as plan">
      <kpn-plan-action-button
        *ngIf="showUndoButton$ | async"
        (action)="undo()"
        [enabled]="undoEnabled()"
        icon="undo"
        text="Undo"
        i18n-text="@@planner.action.undo"
        title="Undo the previous action"
        i18n-title="@@planner.action.undo.title"
      />

      <kpn-plan-action-button
        *ngIf="showRedoButton$ | async"
        (action)="redo()"
        [enabled]="redoEnabled()"
        icon="redo"
        text="Redo"
        i18n-text="@@planner.action.redo"
        title="Redo the action that was previously undone"
        i18n-title="@@planner.action.redo.title"
      />

      <kpn-plan-action-button
        *ngIf="showResetButton$ | async"
        (action)="reset()"
        [enabled]="hasStartNode(plan)"
        icon="reset"
        text="Reset"
        i18n-text="@@planner.action.reset"
        title="Wipe out current route plan and restart route planning from scratch"
        i18n-title="@@planner.action.reset.title"
      />

      <kpn-plan-action-button
        *ngIf="showReverseButton$ | async"
        (action)="reverse()"
        [enabled]="hasRoute(plan)"
        icon="reverse"
        text="Reverse"
        i18n-text="@@planner.action.reverse"
        title="Reverse the route direction (startnode becomes endnode, and vice versa)"
        i18n-title="@@planner.action.reverse.title"
      />

      <kpn-plan-action-button
        (action)="output()"
        [enabled]="hasRoute(plan)"
        icon="output"
        text="Output"
        i18n-text="@@planner.action.output"
        title="Output planned route"
        i18n-title="@@planner.action.output.title"
      />
    </div>
  `,
  styles: `
    .buttons {
      display: inline-block;
      padding-top: 15px;
      padding-bottom: 15px;
    }
  `,
  standalone: true,
  imports: [NgIf, PlanActionButtonComponent, AsyncPipe],
})
export class PlanActionsComponent implements OnInit {
  plan$: Observable<Plan>;

  showUndoButton$: Observable<boolean>;
  showRedoButton$: Observable<boolean>;
  showResetButton$: Observable<boolean>;
  showReverseButton$: Observable<boolean>;

  constructor(
    private plannerService: PlannerService,
    private pageWidthService: PageWidthService,
    private dialog: MatDialog,
    private injector: Injector
  ) {}

  ngOnInit(): void {
    this.plan$ = this.plannerService.context.plan$;
    this.showUndoButton$ = this.pageWidthService.current$.pipe(
      map((pageWidth) => pageWidth !== PageWidth.veryVerySmall)
    );
    this.showRedoButton$ = this.showUndoButton$;
    this.showResetButton$ = this.pageWidthService.current$.pipe(
      map(
        (pageWidth) =>
          pageWidth !== PageWidth.verySmall &&
          pageWidth !== PageWidth.veryVerySmall
      )
    );
    this.showReverseButton$ = this.showResetButton$;
  }

  undo(): void {
    this.plannerService.context.undo();
  }

  redo(): void {
    this.plannerService.context.redo();
  }

  reset(): void {
    const command = new PlannerCommandReset();
    this.plannerService.context.execute(command);
  }

  reverse(): void {
    const oldPlan = this.plannerService.context.plan;
    new PlanReverser(this.plannerService.context).reverse(oldPlan).subscribe({
      next: (newPlan) => {
        const command = new PlannerCommandReverse(oldPlan, newPlan);
        this.plannerService.context.execute(command);
      },
      error: (error) => this.plannerService.context.errorDialog(error),
    });
  }

  output(): void {
    const injector = Injector.create({
      parent: this.injector,
      providers: [{ provide: PlannerService, useValue: this.plannerService }],
    });

    this.dialog.open(PlanOutputDialogComponent, {
      minWidth: 280,
      autoFocus: false,
      injector,
    });
  }

  undoEnabled(): boolean {
    return this.plannerService.context.commandStack.canUndo;
  }

  redoEnabled(): boolean {
    return this.plannerService.context.commandStack.canRedo;
  }

  hasStartNode(plan: Plan): boolean {
    return plan.sourceNode !== null;
  }

  hasRoute(plan: Plan): boolean {
    return !plan.legs.isEmpty();
  }
}
