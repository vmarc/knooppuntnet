import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { Injector } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PageWidthService } from '@app/shared/components/page-width.service';
import { NzModalService } from 'ng-zorro-antd/modal';
import { PlannerCommandReset } from '../../../domain/commands/planner-command-reset';
import { PlannerCommandReverse } from '../../../domain/commands/planner-command-reverse';
import { Plan } from '../../../domain/plan/plan';
import { PlanReverser } from '../../../domain/plan/plan-reverser';
import { PlannerService } from '../planner.service';
import { PlanActionButtonComponent } from './plan-action-button.component';
import { PlanOutputDialogComponent } from './plan-output-dialog.component';

@Component({
  selector: 'ui-plan-actions',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (plan(); as plan) {
      <div class="buttons">
        @if (showUndoButton()) {
          <ui-plan-action-button
            (action)="undo()"
            [enabled]="undoEnabled()"
            icon="undo"
            text="Undo"
            i18n-text="@@planner.action.undo"
            title="Undo the previous action"
            i18n-title="@@planner.action.undo.title"
          />
        }
        @if (showRedoButton()) {
          <ui-plan-action-button
            (action)="redo()"
            [enabled]="redoEnabled()"
            icon="redo"
            text="Redo"
            i18n-text="@@planner.action.redo"
            title="Redo the action that was previously undone"
            i18n-title="@@planner.action.redo.title"
          />
        }
        @if (showResetButton()) {
          <ui-plan-action-button
            (action)="reset()"
            [enabled]="hasStartNode(plan)"
            icon="poweroff"
            text="Reset"
            i18n-text="@@planner.action.reset"
            title="Wipe out current route plan and restart route planning from scratch"
            i18n-title="@@planner.action.reset.title"
          />
        }
        @if (showReverseButton()) {
          <ui-plan-action-button
            (action)="reverse()"
            [enabled]="hasRoute(plan)"
            icon="swap"
            text="Reverse"
            i18n-text="@@planner.action.reverse"
            title="Reverse the route direction (startnode becomes endnode, and vice versa)"
            i18n-title="@@planner.action.reverse.title"
          />
        }
        <ui-plan-action-button
          (action)="output()"
          [enabled]="hasRoute(plan)"
          icon="export"
          text="Output"
          i18n-text="@@planner.action.output"
          title="Output planned route"
          i18n-title="@@planner.action.output.title"
        />
      </div>
    }
  `,
  styles: `
    .buttons {
      display: inline-block;
      padding-top: 15px;
      padding-bottom: 15px;
    }
  `,
  imports: [PlanActionButtonComponent],
})
export class PlanActionsComponent {
  private readonly plannerService = inject(PlannerService);
  private readonly pageWidthService = inject(PageWidthService);
  private readonly modelService = inject(NzModalService);
  private readonly injector = inject(Injector);

  readonly plan = this.plannerService.context.plan;
  protected readonly showUndoButton = computed(() => !this.pageWidthService.isVeryVerySmall());
  protected readonly showRedoButton = this.showUndoButton;
  protected readonly showResetButton = computed(
    () => !(this.pageWidthService.isVerySmall() || this.pageWidthService.isVeryVerySmall())
  );
  protected readonly showReverseButton = this.showResetButton;

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
    const oldPlan = this.plannerService.context.plan();
    new PlanReverser(this.plannerService.context).reverse(oldPlan).subscribe({
      next: (newPlan) => {
        const command = new PlannerCommandReverse(oldPlan, newPlan);
        this.plannerService.context.execute(command);
      },
      error: (error) => this.plannerService.context.errorDialog(error),
    });
  }

  output(): void {
    this.modelService.create({
      nzContent: PlanOutputDialogComponent,
      nzFooter: null,
    });
  }

  undoEnabled(): boolean {
    return this.plannerService.context.commandStack().canUndo;
  }

  redoEnabled(): boolean {
    return this.plannerService.context.commandStack().canRedo;
  }

  hasStartNode(plan: Plan): boolean {
    return plan.sourceNode !== null;
  }

  hasRoute(plan: Plan): boolean {
    return !plan.legs.isEmpty();
  }
}
