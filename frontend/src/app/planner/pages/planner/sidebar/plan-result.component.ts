import { inject } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { PlannerStateService } from '../planner-state.service';
import { PlannerService } from '../planner.service';
import { PlanCompactComponent } from './plan-compact.component';
import { PlanDetailedComponent } from './plan-detailed.component';
import { PlanDistanceComponent } from './plan-distance.component';
import { PlanInstructionsComponent } from './plan-instructions.component';

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
      @if (instructions()) {
        <kpn-plan-instructions [plan]="plan" />
      }
    }
  `,
  standalone: true,
  imports: [
    PlanCompactComponent,
    PlanDetailedComponent,
    PlanDistanceComponent,
    PlanInstructionsComponent,
  ],
})
export class PlanResultComponent {
  private readonly plannerService = inject(PlannerService);
  private readonly plannerStateService = inject(PlannerStateService);

  protected readonly compact = this.plannerStateService.resultModeCompact;
  protected readonly detailed = this.plannerStateService.resultModeDetailed;
  protected readonly instructions = this.plannerStateService.resultModeInstructions;
  protected readonly plan = this.plannerService.context.plan;
}
