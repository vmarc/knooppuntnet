import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
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
  compact$: Observable<boolean>;
  detailed$: Observable<boolean>;
  instructions$: Observable<boolean>;
  plan$: Observable<Plan>;

  constructor(private plannerService: PlannerService) {}

  ngOnInit(): void {
    this.compact$ = this.plannerService.resultMode$.pipe(
      map((mode) => mode === 'compact')
    );
    this.detailed$ = this.plannerService.resultMode$.pipe(
      map((mode) => mode === 'detailed')
    );
    this.instructions$ = this.plannerService.resultMode$.pipe(
      map((mode) => mode === 'instructions')
    );
    this.plan$ = this.plannerService.context.plan$;
  }
}
