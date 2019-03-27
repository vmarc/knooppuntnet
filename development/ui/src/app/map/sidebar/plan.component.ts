import {Component} from '@angular/core';
import {PlannerService} from "../planner.service";
import {Plan} from "../pages/tryout2/plan";

@Component({
  selector: 'kpn-plan',
  template: `
    <div *ngFor="let member of plan.members">
      <div *ngIf="member.isUserSelectedNode()">
        user selected node
      </div>
      <div *ngIf="member.isServerPlannedNode()">
        server planned node
      </div>
      <div *ngIf="member.isLeg()">
        leg
      </div>
    </div>
  `,
  styles: [`
  `]
})
export class PlanComponent {

  plan: Plan;

  constructor(private plannerService: PlannerService) {
    plannerService.context.plan.subscribe(plan => this.plan = plan);
  }

}
