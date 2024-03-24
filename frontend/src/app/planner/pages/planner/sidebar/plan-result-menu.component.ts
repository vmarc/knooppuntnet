import { NgClass } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { PreferencesService } from '@app/core';
import { MapResultMode } from '../../../../ol/services/map-result-mode';
import { PlannerStateService } from '../../../planner-state.service';

@Component({
  selector: 'kpn-plan-result-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (resultMode(); as resultMode) {
      <div class="menu">
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
        @if (instructions()) {
          <span>
            <a
              [ngClass]="{ selected: resultMode === 'instructions' }"
              (click)="resultModeInstructions($event)"
              i18n="@@planner.instructions"
            >
              Instructions
            </a>
          </span>
        }
      </div>
    }
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
  imports: [NgClass],
})
export class PlanResultMenuComponent {
  private readonly plannerStateService = inject(PlannerStateService);
  private readonly preferencesService = inject(PreferencesService);

  readonly resultMode = this.plannerStateService.resultMode;
  readonly instructions = this.preferencesService.instructions;

  resultModeCompact(event) {
    this.handleResultMode(event, 'compact');
  }

  resultModeDetailed(event) {
    this.handleResultMode(event, 'detailed');
  }

  resultModeInstructions(event) {
    this.handleResultMode(event, 'instructions');
  }

  private handleResultMode(event, resultMode: MapResultMode) {
    this.plannerStateService.setResultMode(resultMode);
    event.stopPropagation();
  }
}
