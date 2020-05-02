import {ChangeDetectionStrategy} from "@angular/core";
import {Input} from "@angular/core";
import {Component} from "@angular/core";
import {BarChart2D} from "../../../kpn/api/common/status/bar-chart2d";

/* tslint:disable:template-i18n English only */
@Component({
  selector: "kpn-action-bar-chart-stacked",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ngx-charts-bar-vertical-stacked
      [view]="view"
      [results]="barChart.data"
      [xAxis]="true"
      [yAxis]="true"
      [showXAxisLabel]="true"
      [showYAxisLabel]="true"
      [xAxisLabel]="xAxisLabel"
      [yAxisLabel]="yAxisLabel"
      [legend]="true"
      [roundDomains]="false"
      [showDataLabel]="false"
      (select)="onSelect($event)">
    </ngx-charts-bar-vertical-stacked>
  `
})
export class ActionBarChartStackedComponent {

  @Input() barChart: BarChart2D;
  @Input() xAxisLabel: string;
  @Input() yAxisLabel: string;

  view: [number, number] = [850, 300];

  onSelect(event) {
    console.log(event);
  }
}
