import {Input} from "@angular/core";
import {Component} from "@angular/core";
import {BarChart} from "../../../kpn/api/common/status/bar-chart";

@Component({
  selector: "kpn-action-bar-chart",
  template: `
    <ngx-charts-bar-vertical
      [view]="view"
      [results]="barChart.data"
      [xAxis]="true"
      [yAxis]="true"
      [showXAxisLabel]="true"
      [showYAxisLabel]="true"
      [xAxisLabel]="xAxisLabel"
      [yAxisLabel]="yAxisLabel"
      [legend]="false"
      [roundDomains]="false"
      [roundEdges]="false"
      [showDataLabel]="false"
      (select)="onSelect($event)">
    </ngx-charts-bar-vertical>
  `
})
export class ActionBarChartComponent {

  @Input() barChart: BarChart;
  @Input() xAxisLabel: string;
  @Input() yAxisLabel: string;

  view: [number, number] = [700, 300];

  onSelect(event) {
    console.log(event);
  }
}
