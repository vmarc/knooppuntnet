import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { BarChart2D } from '@api/common/status/bar-chart2d';

/* tslint:disable:template-i18n English only */
@Component({
  selector: 'kpn-delay-chart',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
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
