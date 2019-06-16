import {ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges} from "@angular/core";
import {Plan} from "../planner/plan/plan";

@Component({
  selector: "kpn-plan-distance",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="meters > 0" class="distance">
      <span i18n="@@plan.distance">Distance</span>: {{distance()}}
    </div>
  `,
  styles: [`
    .distance {
      margin-bottom: 10px;
      color: gray;
    }
  `]
})
export class PlanDistanceComponent implements OnChanges {

  @Input() plan: Plan;

  meters: number = 0;

  ngOnChanges(changes: SimpleChanges): void {
    this.meters = this.plan.meters();
  }

  distance(): string {
    if (this.meters > 1000) {
      const km = (this.meters / 1000).toFixed(1).toString().replace(".", ",");
      return `${km} km`;
    }
    return `${this.meters} m`;
  }

}
