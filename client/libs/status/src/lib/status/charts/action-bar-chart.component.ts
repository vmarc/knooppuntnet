import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { BarChart } from '@api/common/status';
import { BarChartModule } from '@swimlane/ngx-charts';

@Component({
  selector: 'kpn-action-bar-chart',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
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
      (select)="onSelect($event)"
    />
  `,
  standalone: true,
  imports: [BarChartModule],
})
export class ActionBarChartComponent {
  @Input({ required: true }) barChart: BarChart;
  @Input() xAxisLabel: string;
  @Input() yAxisLabel: string;

  view: [number, number] = [700, 300];

  onSelect(event) {
    console.log(event);
  }
}
