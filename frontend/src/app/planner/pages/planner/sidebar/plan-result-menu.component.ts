import { NgIf } from '@angular/common';
import { NgClass } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { selectPreferencesInstructions } from '@app/core';
import { Store } from '@ngrx/store';
import { PlannerService } from '../../../planner.service';
import { actionPlannerResultMode } from '../../../store/planner-actions';
import { selectPlannerResultMode } from '../../../store/planner-selectors';

@Component({
  selector: 'kpn-plan-result-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="menu" *ngIf="plannerResultMode() as resultMode">
      <span>
        <a
          [ngClass]="{ selected: resultMode === 'compact' }"
          (click)="resultModeCompact($event)"
          i18n="@@planner.compact"
        >
          Compact
        </a>
      </span>
      <span>
        <a
          [ngClass]="{ selected: resultMode === 'detailed' }"
          (click)="resultModeDetailed($event)"
          i18n="@@planner.detailed"
        >
          Detailed
        </a>
      </span>
      <span *ngIf="instructions()">
        <a
          [ngClass]="{ selected: resultMode === 'instructions' }"
          (click)="resultModeInstructions($event)"
          i18n="@@planner.instructions"
        >
          Instructions
        </a>
      </span>
    </div>
  `,
  styles: `
    .menu {
      padding-bottom: 5px;
    }

    .menu :not(:last-child):after {
      content: ' | ';
      padding-left: 5px;
      padding-right: 5px;
    }

    a.selected {
      color: rgba(0, 0, 0, 0.87);
      font-weight: bold;
    }
  `,
  standalone: true,
  imports: [NgIf, NgClass, AsyncPipe],
})
export class PlanResultMenuComponent {
  readonly plannerResultMode = this.store.selectSignal(selectPlannerResultMode);
  readonly instructions = this.store.selectSignal(
    selectPreferencesInstructions
  );

  constructor(
    private plannerService: PlannerService,
    private store: Store
  ) {}

  resultModeCompact(event) {
    this.handleResultMode(event, 'compact');
  }

  resultModeDetailed(event) {
    this.handleResultMode(event, 'detailed');
  }

  resultModeInstructions(event) {
    this.handleResultMode(event, 'instructions');
  }

  private handleResultMode(event, resultMode: string) {
    this.store.dispatch(actionPlannerResultMode({ resultMode }));
    event.stopPropagation();
  }
}
