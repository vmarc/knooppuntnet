import { NgIf } from '@angular/common';
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
    <div *ngIf="plan$ | async as plan">
      <kpn-plan-distance [plan]="plan" />
      <kpn-plan-compact *ngIf="compact$ | async" [plan]="plan" />
      <kpn-plan-detailed *ngIf="detailed$ | async" [plan]="plan" />
      <kpn-plan-instructions *ngIf="instructions$ | async" [plan]="plan" />
    </div>
  `,
  standalone: true,
  imports: [
    NgIf,
    PlanDistanceComponent,
    PlanCompactComponent,
    PlanDetailedComponent,
    PlanInstructionsComponent,
    AsyncPipe,
  ],
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
