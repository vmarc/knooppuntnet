import {Input} from "@angular/core";
import {Component} from "@angular/core";
import {BarChart} from "../../../kpn/api/common/status/bar-chart";

/* tslint:disable:template-i18n English only */
@Component({
  selector: "kpn-replication-elements",
  template: `
    <h2>
      Replication element count
    </h2>
    <div class="chart">
      <kpn-action-bar-chart
        [barChart]="barChart"
        [xAxisLabel]="xAxisLabel"
        yAxisLabel="Elements">
      </kpn-action-bar-chart>
    </div>
  `
})
export class ReplicationElementsComponent {
  @Input() barChart: BarChart;
  @Input() xAxisLabel: string;
}
