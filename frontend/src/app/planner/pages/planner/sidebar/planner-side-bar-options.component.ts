import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { State } from '@app/state';
import { NzCheckboxComponent } from 'ng-zorro-antd/checkbox';

@Component({
  selector: 'kpn-planner-sidebar-options',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <label nz-checkbox [nzChecked]="showProposed()" (nzCheckedChange)="showProposedChanged($event)">
      <span i18n="@@planner.options.show-proposed">Show proposed routes</span>
    </label>

    <label nz-checkbox [nzChecked]="planProposed()" (nzCheckedChange)="planProposedChanged($event)">
      <span i18n="@@planner.options.plan-proposed">Allow planning proposed routes</span>
    </label>
  `,
  imports: [NzCheckboxComponent],
})
export class PlannerSideBarOptionsComponent {
  private readonly state = inject(State);

  protected readonly showProposed = this.state.preferences.showProposed;
  protected readonly planProposed = this.state.preferences.planProposed;

  showProposedChanged(checked: boolean) {
    this.state.preferences.updateShowProposed(checked);
  }

  planProposedChanged(checked: boolean) {
    this.state.preferences.updatePlanProposed(checked);
  }
}
