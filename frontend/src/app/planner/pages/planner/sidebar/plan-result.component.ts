import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { State } from '@app/state/state';
import { PlannerService } from '../planner.service';
import { PlanCompactComponent } from './plan-compact.component';
import { PlanDetailedComponent } from './plan-detailed.component';
import { PlanDistanceComponent } from './plan-distance.component';

@Component({
  selector: 'kpn-plan-result',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (plan(); as plan) {
      <kpn-plan-distance [plan]="plan" />
      @if (compact()) {
        <kpn-plan-compact [plan]="plan" />
      }
      @if (detailed()) {
        <kpn-plan-detailed [plan]="plan" />
      }
    }
  `,
  imports: [PlanCompactComponent, PlanDetailedComponent, PlanDistanceComponent],
})
export class PlanResultComponent {
  private readonly state = inject(State);
  private readonly plannerService = inject(PlannerService);
  readonly compact = computed(() => this.state.planner.resultMode() === 'compact');
  readonly detailed = computed(() => this.state.planner.resultMode() === 'detailed');
  protected readonly plan = this.plannerService.context.plan;
}
