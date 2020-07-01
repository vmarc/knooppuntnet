import {ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges} from "@angular/core";
import {Plan} from "../../kpn/api/common/planner/plan";
import {PlanUtil} from "../planner/plan/plan-util";

@Component({
  selector: "kpn-plan-distance",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="meters > 0" class="distance">
      <span i18n="@@plan.distance" class="kpn-label">Distance</span>{{distance()}}
      <span i18n="@@plan.unpaved" class="kpn-label">Unpaved</span>{{unpaved()}}
    </div>
  `,
  styles: [`
    .distance {
      margin-bottom: 10px;
      color: gray;
    }

    .distance :last-child {
      padding-left: 20px;
    }
  `]
})
export class PlanDistanceComponent implements OnChanges {

  @Input() plan: Plan;

  meters = 0;

  ngOnChanges(changes: SimpleChanges): void {
    this.meters = PlanUtil.planMeters(this.plan);
  }

  distance(): string {
    if (this.meters > 1000) {
      const km = (this.meters / 1000).toFixed(1).toString().replace(".", ",");
      return `${km} km`;
    }
    return `${this.meters} m`;
  }

  unpaved(): string {
    return PlanUtil.planUnpavedPercentage(this.plan);
  }

}
