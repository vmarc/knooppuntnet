import {Input} from "@angular/core";
import {Component} from "@angular/core";
import {BarChart} from "../../../../kpn/api/common/status/bar-chart";

/* tslint:disable:template-i18n english only */
@Component({
  selector: "kpn-docs-chart",
  template: `
    <h2>
      Document count
    </h2>
    <div class="chart">
      <kpn-action-bar-chart
        [barChart]="barChart"
        [xAxisLabel]="xAxisLabel"
        yAxisLabel="documents">
      </kpn-action-bar-chart>
    </div>
  `
})
export class DocsChartComponent {
  @Input() barChart: BarChart;
  @Input() xAxisLabel: string;
}
