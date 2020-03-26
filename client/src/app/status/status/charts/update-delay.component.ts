import {Input} from "@angular/core";
import {Component} from "@angular/core";
import {BarChart} from "../../../kpn/api/common/status/bar-chart";

/* tslint:disable:template-i18n English only */
@Component({
  selector: "kpn-update-delay",
  template: `
    <h2>
      Update average delay
    </h2>
    <div class="chart">
      <kpn-action-bar-chart
        [barChart]="barChart"
        [xAxisLabel]="xAxisLabel"
        yAxisLabel="Average delay">
      </kpn-action-bar-chart>
    </div>
  `
})
export class UpdateDelayComponent {
  @Input() barChart: BarChart;
  @Input() xAxisLabel: string;
}
