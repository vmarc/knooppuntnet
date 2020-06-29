import {ChangeDetectionStrategy, Component, Input} from "@angular/core";
import {PlannerService} from "../planner.service";
import {Plan} from "../planner/plan/plan";
import {PlanRoute} from "../planner/plan/plan-route";

@Component({
  selector: "kpn-plan-detailed",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <div *ngIf="plan.sourceNode !== null" class="node user-selected">
      <div class="text">
        {{plan.sourceNode.nodeName}}
      </div>
    </div>

    <div *ngFor="let leg of plan.legs">
      <div *ngIf="leg.routes.isEmpty()">
        <div class="leg">
          Calculating...
        </div>
        <div class="node">
          <div class="text">
            {{leg.sinkNode.nodeName}}
          </div>
        </div>
      </div>
      <div *ngFor="let legRoute of leg.routes; let i = index">
        <div class="leg">
          {{legRoute.meters}} m
          <span *ngIf="hasColour(legRoute)" class="colour">
            {{colours(legRoute)}}
          </span>
        </div>
        <div class="node" [class.server-selected]="i < leg.routes.size - 1">
          <div class="text">
            {{legRoute.sinkNode.nodeName}}
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`

    .leg {
      padding-top: 5px;
      padding-bottom: 5px;
      padding-left: 35px;
    }

    .node {
      display: inline-block;
      border-color: gray;
      border-radius: 50%;
      border-style: solid;
      border-width: 3px;
      width: 30px;
      height: 30px;
    }

    .server-selected {
      border-width: 1px;
      padding-left: 2px;
    }

    .text {
      width: 30px;
      margin-top: 5px;
      text-align: center;
    }

    .colour {
      padding-left: 6px;
    }

  `]
})
export class PlanDetailedComponent {

  @Input() plan: Plan;

  constructor(private plannerService: PlannerService) {
  }

  hasColour(planRoute: PlanRoute): boolean {
    return this.plannerService.hasColour(planRoute);
  }

  colours(planRoute: PlanRoute): string {
    return this.plannerService.colours(planRoute);
  }
}
