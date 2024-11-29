import { inject } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { PlannerStateService } from '../planner-state.service';
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
  private readonly plannerService = inject(PlannerService);
  private readonly plannerStateService = inject(PlannerStateService);

  protected readonly compact = this.plannerStateService.resultModeCompact;
  protected readonly detailed = this.plannerStateService.resultModeDetailed;
  protected readonly plan = this.plannerService.context.plan;
}
