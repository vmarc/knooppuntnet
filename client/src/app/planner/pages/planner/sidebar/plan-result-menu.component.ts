import { ChangeDetectionStrategy, Component } from '@angular/core';
import { selectPreferencesInstructions } from '@app/core/preferences/preferences.selectors';
import { Store } from '@ngrx/store';
import { PlannerService } from '../../../services/planner.service';
import { actionPlannerResultMode } from '../../../store/planner-actions';
import { selectPlannerResultMode } from '../../../store/planner-selectors';

@Component({
  selector: 'kpn-plan-result-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="menu" *ngIf="mode$ | async as mode">
      <span>
        <a
          [ngClass]="{ selected: mode === 'compact' }"
          (click)="compact($event)"
          i18n="@@planner.compact"
        >
          Compact
        </a>
      </span>
      <span>
        <a
          [ngClass]="{ selected: mode === 'detailed' }"
          (click)="detailed($event)"
          i18n="@@planner.detailed"
        >
          Detailed
        </a>
      </span>
      <span *ngIf="instructions$ | async">
        <a
          [ngClass]="{ selected: mode === 'instructions' }"
          (click)="instructions($event)"
          i18n="@@planner.instructions"
        >
          Instructions
        </a>
      </span>
    </div>
  `,
  styles: [
    `
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
  ],
})
export class PlanResultMenuComponent {
  mode$ = this.store.select(selectPlannerResultMode);
  readonly instructions$ = this.store.select(selectPreferencesInstructions);

  constructor(private plannerService: PlannerService, private store: Store) {}

  compact(event) {
    this.handleResultMode(event, 'compact');
  }

  detailed(event) {
    this.handleResultMode(event, 'detailed');
  }

  instructions(event) {
    this.handleResultMode(event, 'instructions');
  }

  private handleResultMode(event, resultMode: string) {
    this.store.dispatch(actionPlannerResultMode({ resultMode }));
    event.stopPropagation();
  }
}
