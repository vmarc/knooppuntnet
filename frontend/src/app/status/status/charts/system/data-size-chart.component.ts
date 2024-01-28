import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { BarChart } from '@api/common/status';
import { ActionBarChartComponent } from '../action-bar-chart.component';

@Component({
  selector: 'kpn-data-size-chart',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <h2>Data size</h2>
    <div class="chart">
      <kpn-action-bar-chart
        [barChart]="barChart()"
        [xAxisLabel]="xAxisLabel()"
        yAxisLabel="TODO bytes?"
      />
    </div>
  `,
  standalone: true,
  imports: [ActionBarChartComponent],
})
export class DataSizeChartComponent {
  barChart = input<BarChart | undefined>();
  xAxisLabel = input<string | undefined>();
}
