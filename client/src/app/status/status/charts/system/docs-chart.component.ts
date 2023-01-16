import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { BarChart } from '@api/common/status/bar-chart';

@Component({
  selector: 'kpn-docs-chart',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <h2>Document count</h2>
    <div class="chart">
      <kpn-action-bar-chart
        [barChart]="barChart"
        [xAxisLabel]="xAxisLabel"
        yAxisLabel="documents"
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
        [yAxisLabel]="'documents'"
        [legend]="false"
        [roundDomains]="false"
      >
      </ngx-charts-line-chart>
    </div>
  `,
})
export class DocsChartComponent {
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
