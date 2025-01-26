import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatExpansionModule } from '@angular/material/expansion';
import { State } from '@app/state';

@Component({
  selector: 'kpn-planner-sidebar-options',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-expansion-panel [expanded]="expanded()" (expandedChange)="expandedChanged($event)">
      <mat-expansion-panel-header i18n="@@planner.options"> Options</mat-expansion-panel-header>
      <p>
        <mat-checkbox [checked]="showProposed()" (change)="showProposedChanged($event)">
          <span i18n="@@planner.options.show-proposed">Show proposed routes</span>
        </mat-checkbox>
        <mat-checkbox [checked]="planProposed()" (change)="planProposedChanged($event)">
          <span i18n="@@planner.options.plan-proposed">Allow planning proposed routes</span>
        </mat-checkbox>
      </p>
    </mat-expansion-panel>
  `,
  styles: `
    .legend > div {
      display: flex;
      align-items: center;
    }
  `,
  imports: [MatExpansionModule, MatCheckboxModule],
})
export class PlannerSideBarOptionsComponent {
  private readonly state = inject(State);

  readonly expanded = this.state.preferences.showOptions;
  protected readonly showProposed = this.state.preferences.showProposed;
  protected readonly planProposed = this.state.preferences.planProposed;

  expandedChanged(expanded: boolean): void {
    this.state.preferences.updateShowOptions(expanded);
  }

  showProposedChanged(event: MatCheckboxChange) {
    this.state.preferences.updateShowProposed(event.checked);
  }

  planProposedChanged(event: MatCheckboxChange) {
    this.state.preferences.updatePlanProposed(event.checked);
  }
}
