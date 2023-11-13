import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { BarChart2D } from '@api/common/status';
import { BarChartModule } from '@swimlane/ngx-charts';

@Component({
  selector: 'kpn-action-bar-chart-stacked',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
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
      (select)="onSelect($event)"
    />
  `,
  standalone: true,
  imports: [BarChartModule],
})
export class ActionBarChartStackedComponent {
  @Input({ required: true }) barChart: BarChart2D;
  @Input() xAxisLabel: string;
  @Input() yAxisLabel: string;

  view: [number, number] = [850, 300];

  onSelect(event) {
    console.log(event);
  }
}
