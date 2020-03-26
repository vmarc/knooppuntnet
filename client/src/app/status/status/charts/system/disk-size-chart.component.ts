import {Input} from "@angular/core";
import {Component} from "@angular/core";
import {BarChart} from "../../../../kpn/api/common/status/bar-chart";

/* tslint:disable:template-i18n english only */
@Component({
  selector: "kpn-disk-size-chart",
  template: `
    <h2>
      Disk size
    </h2>
    <div class="chart">
      <kpn-action-bar-chart
        [barChart]="barChart"
        [xAxisLabel]="xAxisLabel"
        yAxisLabel="TODO bytes?">
      </kpn-action-bar-chart>
    </div>
  `
})
export class DiskSizeChartComponent {
  @Input() barChart: BarChart;
  @Input() xAxisLabel: string;
}
