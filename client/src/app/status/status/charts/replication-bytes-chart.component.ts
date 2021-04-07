import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { BarChart } from '@api/common/status/bar-chart';

/* tslint:disable:template-i18n English only */
@Component({
  selector: 'kpn-replication-bytes-chart',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <h2>Replication bytes</h2>
    <div class="chart">
      <kpn-action-bar-chart
        [barChart]="barChart"
        [xAxisLabel]="xAxisLabel"
        yAxisLabel="Bytes downloaded"
      >
      </kpn-action-bar-chart>
    </div>
  `,
})
export class ReplicationBytesChartComponent {
  @Input() barChart: BarChart;
  @Input() xAxisLabel: string;
}
