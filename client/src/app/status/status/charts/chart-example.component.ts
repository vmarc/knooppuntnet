import {Input} from "@angular/core";
import {Component} from "@angular/core";
import {BarChart2D} from "../../../kpn/api/common/status/bar-chart2d";

@Component({
  selector: "kpn-chart-example",
  template: `
    <ngx-charts-bar-vertical-2d
      [view]="view"
      [results]="barChart.data"
      [xAxis]="true"
      [yAxis]="true"
      [showXAxisLabel]="true"
      [showYAxisLabel]="true"
      [xAxisLabel]="barChart.xAxisLabel"
      [yAxisLabel]="barChart.yAxisLabel"
      [legend]="true"
      [legendTitle]="barChart.legendTitle"
      [roundDomains]="false"
      [roundEdges]="false"
      [showDataLabel]="false"
      (select)="onSelect($event)">
    </ngx-charts-bar-vertical-2d>
  `
})
export class ChartExampleComponent {

  @Input() barChart: BarChart2D;

  view: [number, number] = [700, 300];

  onSelect(event) {
    console.log(event);
  }
}
