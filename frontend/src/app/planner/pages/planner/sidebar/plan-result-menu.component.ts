import { NgClass } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { State } from '@app/state/state';
import { MapResultMode } from '@app/ol/services/map-result-mode';

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
  imports: [NgClass],
})
export class PlanResultMenuComponent {
  private readonly state = inject(State);
  readonly resultMode = this.state.planner.resultMode;

  resultModeCompact(event) {
    this.handleResultMode(event, 'compact');
  }

  resultModeDetailed(event) {
    this.handleResultMode(event, 'detailed');
  }

  private handleResultMode(event, resultMode: MapResultMode) {
    this.state.planner.updateResultMode(resultMode);
    event.stopPropagation();
  }
}
