import {ChangeDetectionStrategy, Component, Input} from "@angular/core";
import {Plan} from "../planner/plan/plan";

@Component({
  selector: "kpn-plan-detailed",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <div *ngIf="plan.source !== null" class="node user-selected">
      <div class="text">
        {{plan.source.nodeName}}
      </div>
    </div>

    <div *ngFor="let leg of plan.legs">
      <div *ngIf="leg.routes.isEmpty()">
        <div class="leg">
          Calculating...
        </div>
        <div class="node">
          <div class="text">
            {{leg.sink.nodeName}}
          </div>
        </div>
      </div>
      <div *ngFor="let legRoute of leg.routes; let i = index">
        <div class="leg">
          {{legRoute.meters}} m
        </div>
        <div class="node" [class.server-selected]="i < leg.routes.size - 1">
          <div class="text">
            {{legRoute.sink.nodeName}}
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

  `]
})
export class PlanDetailedComponent {
  @Input() plan: Plan;
}
