import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { BarChart2D } from '@api/common/status/bar-chart2d';

@Component({
  selector: 'kpn-delay-chart',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <h2>Average delay</h2>
    <div class="chart">
      <kpn-action-bar-chart-stacked
        [barChart]="barChart"
        [xAxisLabel]="xAxisLabel"
        yAxisLabel="Average delay"
      >
      </kpn-action-bar-chart-stacked>
    </div>
  `,
})
export class DelayChartComponent {
  @Input() barChart: BarChart2D;
  @Input() xAxisLabel: string;
}
