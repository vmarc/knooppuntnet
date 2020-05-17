import {ChangeDetectionStrategy, Component, OnInit} from "@angular/core";
import {Observable} from "rxjs";
import {PlannerService} from "../planner.service";
import {Plan} from "../planner/plan/plan";

@Component({
  selector: "kpn-plan-result",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="plan$ | async as plan">
      <kpn-plan-distance [plan]="plan"></kpn-plan-distance>
      <kpn-plan-compact *ngIf="mode == 'compact'" [plan]="plan"></kpn-plan-compact>
      <kpn-plan-detailed *ngIf="mode == 'detailed'" [plan]="plan"></kpn-plan-detailed>
      <kpn-plan-instructions *ngIf="mode == 'instructions'" [plan]="plan"></kpn-plan-instructions>
    </div>
  `,
  styles: []
})

export class PlanResultComponent implements OnInit {

  mode = "compact";

  plan$: Observable<Plan>;

  constructor(private plannerService: PlannerService) {
  }

  ngOnInit(): void {
    this.plan$ = this.plannerService.context.planObserver;
  }
}
