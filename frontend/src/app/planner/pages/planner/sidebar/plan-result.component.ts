import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { Plan } from '../../../domain/plan/plan';
import { PlannerService } from '../../../planner.service';
import { selectPlannerResultModeInstructions } from '../../../store/planner-selectors';
import { selectPlannerResultModeDetailed } from '../../../store/planner-selectors';
import { selectPlannerResultModeCompact } from '../../../store/planner-selectors';
import { PlanCompactComponent } from './plan-compact.component';
import { PlanDetailedComponent } from './plan-detailed.component';
import { PlanDistanceComponent } from './plan-distance.component';
import { PlanInstructionsComponent } from './plan-instructions.component';

@Component({
  selector: 'kpn-plan-result',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (plan$ | async; as plan) {
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
    AsyncPipe,
    PlanCompactComponent,
    PlanDetailedComponent,
    PlanDistanceComponent,
    PlanInstructionsComponent,
  ],
})
export class PlanResultComponent implements OnInit {
  readonly compact = this.store.selectSignal(selectPlannerResultModeCompact);
  readonly detailed = this.store.selectSignal(selectPlannerResultModeDetailed);
  readonly instructions = this.store.selectSignal(selectPlannerResultModeInstructions);
  plan$: Observable<Plan>;

  constructor(
    private plannerService: PlannerService,
    private store: Store
  ) {}

  ngOnInit(): void {
    this.plan$ = this.plannerService.context.plan$;
  }
}
