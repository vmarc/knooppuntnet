import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { BarChart } from '@api/common/status/bar-chart';
import { ActionBarChartComponent } from '../action-bar-chart.component';

@Component({
  selector: 'ui-disk-size-external-chart',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <h2>Disk size external</h2>
    <div class="chart">
      <ui-action-bar-chart
        [barChart]="barChart()"
        [xAxisLabel]="xAxisLabel()"
        yAxisLabel="TODO bytes?"
      />
    </div>
  `,
  imports: [ActionBarChartComponent],
})
export class DiskSizeExternalChartComponent {
  readonly barChart = input.required<BarChart>();
  readonly xAxisLabel = input.required<string>();
}
