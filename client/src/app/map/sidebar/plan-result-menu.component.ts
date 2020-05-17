import {ChangeDetectionStrategy, Component} from "@angular/core";

@Component({
  selector: "kpn-plan-result-menu",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
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
  `,
  styles: [`
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

  `]
})
export class PlanResultMenuComponent {
  mode = "compact";
}
