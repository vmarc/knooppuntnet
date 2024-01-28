import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { BarChart2D } from '@api/common/status';
import { ActionBarChartStackedComponent } from './action-bar-chart-stacked.component';

@Component({
  selector: 'kpn-delay-chart',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <h2>Average delay</h2>
    <div class="chart">
      <kpn-action-bar-chart-stacked
        [barChart]="barChart()"
        [xAxisLabel]="xAxisLabel()"
        yAxisLabel="Average delay"
      />
    </div>
  `,
  standalone: true,
  imports: [ActionBarChartStackedComponent],
})
export class DelayChartComponent {
  barChart = input.required<BarChart2D>();
  xAxisLabel = input.required<string>();
}
