import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { BarChart } from '@api/common/status/bar-chart';

@Component({
  selector: 'kpn-log-api-chart',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <h2>Api</h2>
    <div class="chart">
      <kpn-action-bar-chart
        [barChart]="barChart"
        [xAxisLabel]="xAxisLabel"
        yAxisLabel="requests"
      >
      </kpn-action-bar-chart>
    </div>
  `,
})
export class LogApiChartComponent {
  @Input() barChart: BarChart;
  @Input() xAxisLabel: string;
}
