import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { BarChart } from '@api/common/status';
import { ActionBarChartComponent } from '../action-bar-chart.component';

@Component({
  selector: 'kpn-log-analysis-robot-chart',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <h2>Analysis Robot</h2>
    <div class="chart">
      <kpn-action-bar-chart
        [barChart]="barChart()"
        [xAxisLabel]="xAxisLabel()"
        yAxisLabel="requests"
      />
    </div>
  `,
  standalone: true,
  imports: [ActionBarChartComponent],
})
export class LogAnalysisRobotChartComponent {
  barChart = input<BarChart | undefined>();
  xAxisLabel = input<string | undefined>();
}
