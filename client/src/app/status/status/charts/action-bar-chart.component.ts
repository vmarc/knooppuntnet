import {ChangeDetectionStrategy} from '@angular/core';
import {Input} from '@angular/core';
import {Component} from '@angular/core';
import {BarChart} from '@api/common/status/bar-chart';

/* tslint:disable:template-i18n English only */
@Component({
  selector: 'kpn-action-bar-chart',
  changeDetection: ChangeDetectionStrategy.OnPush,
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
