import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { BarChart } from '@api/common/status';
import { ActionBarChartComponent } from '../action-bar-chart.component';

@Component({
  selector: 'kpn-log-non-robot-chart',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <h2>Non robot</h2>
    <div class="chart">
      <kpn-action-bar-chart
        [barChart]="barChart"
        [xAxisLabel]="xAxisLabel"
        yAxisLabel="requests"
      />
    </div>
  `,
  standalone: true,
  imports: [ActionBarChartComponent],
})
export class LogNonRobotChartComponent {
  @Input() barChart: BarChart;
  @Input() xAxisLabel: string;
}