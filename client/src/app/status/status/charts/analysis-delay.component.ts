import {Input} from "@angular/core";
import {Component} from "@angular/core";
import {BarChart} from "../../../kpn/api/common/status/bar-chart";

/* tslint:disable:template-i18n work-in-progress */
@Component({
  selector: "kpn-analysis-delay",
  template: `
    <h2>
      Analysis average delay
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
export class AnalysisDelayComponent {
  @Input() barChart: BarChart;
  @Input() xAxisLabel: string;
}
