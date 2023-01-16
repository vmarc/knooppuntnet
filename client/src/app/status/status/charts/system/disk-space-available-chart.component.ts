import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { BarChart } from '@api/common/status/bar-chart';

@Component({
  selector: 'kpn-disk-space-available-chart',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <h2>Disk space available</h2>
    <div class="chart">
      <kpn-action-bar-chart
        [barChart]="barChart"
        [xAxisLabel]="xAxisLabel"
        yAxisLabel="TODO bytes?"
      />
    </div>
    <div class="chart">
      <ngx-charts-line-chart
        [view]="view"
        [autoScale]="true"
        [results]="data()"
        [xAxis]="true"
        [yAxis]="true"
        [showXAxisLabel]="true"
        [showYAxisLabel]="true"
        [xAxisLabel]="xAxisLabel"
        [yAxisLabel]="'bytes'"
        [legend]="false"
        [roundDomains]="false"
      />
    </div>
  `,
})
export class DiskSpaceAvailableChartComponent {
  @Input() barChart: BarChart;
  @Input() xAxisLabel: string;

  view: [number, number] = [700, 300];

  data() {
    return [
      {
        name: 'Lesotho',
        series: this.barChart.data,
      },
    ];
  }
}
