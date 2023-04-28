import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { BarChart } from '@api/common/status';

@Component({
  selector: 'kpn-replication-delay-chart',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <h2>Replication average delay</h2>
    <div class="chart">
      <kpn-action-bar-chart
        [barChart]="barChart"
        [xAxisLabel]="xAxisLabel"
        yAxisLabel="Average delay"
      />
    </div>
  `,
})
export class ReplicationDelayChartComponent {
  @Input() barChart: BarChart;
  @Input() xAxisLabel: string;
}
