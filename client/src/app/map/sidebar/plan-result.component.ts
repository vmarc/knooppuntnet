import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { selectPlannerResultModeInstructions } from '@app/map/planner/store/planner-selectors';
import { selectPlannerResultModeDetailed } from '@app/map/planner/store/planner-selectors';
import { selectPlannerResultModeCompact } from '@app/map/planner/store/planner-selectors';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { PlannerService } from '../planner.service';
import { Plan } from '../planner/plan/plan';

@Component({
  selector: 'kpn-plan-result',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="plan$ | async as plan">
      <kpn-plan-distance [plan]="plan"/>
      <kpn-plan-compact
        *ngIf="compact$ | async"
        [plan]="plan"
      />
      <kpn-plan-detailed
        *ngIf="detailed$ | async"
        [plan]="plan"
      />
      <kpn-plan-instructions
        *ngIf="instructions$ | async"
        [plan]="plan"
      />
    </div>
  `,
})
export class PlanResultComponent implements OnInit {
  readonly compact$ = this.store.select(selectPlannerResultModeCompact);
  readonly detailed$ = this.store.select(selectPlannerResultModeDetailed);
  readonly instructions$ = this.store.select(
    selectPlannerResultModeInstructions
  );
  plan$: Observable<Plan>;

  constructor(private plannerService: PlannerService, private store: Store) {}

  ngOnInit(): void {
    this.plan$ = this.plannerService.context.plan$;
  }
}
