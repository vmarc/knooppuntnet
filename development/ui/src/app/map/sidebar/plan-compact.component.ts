import {ChangeDetectionStrategy, Component, Input} from "@angular/core";
import {Plan} from "../planner/plan/plan";

@Component({
  selector: "kpn-plan-compact",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <span *ngIf="plan.source !== null" class="node">
      {{plan.source.nodeName}}
    </span>
    <ng-container *ngFor="let leg of plan.legs">
      <ng-container *ngFor="let legRoute of leg.routes; let i = index">
        <span class="node" [class.visited-node]="i < leg.routes.size - 1">
          {{legRoute.sink.nodeName}}
        </span>
      </ng-container>
    </ng-container>
  `,
  styles: [`

    .node {
      padding-right: 5px;
      font-weight: bold;
    }

    .visited-node {
      font-weight: normal;
    }

  `]
})
export class PlanCompactComponent {
  @Input() plan: Plan;
}
