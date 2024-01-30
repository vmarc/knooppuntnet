import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { BarChart } from '@api/common/status';
import { LineChartModule } from '@swimlane/ngx-charts';
import { ActionBarChartComponent } from '../action-bar-chart.component';

@Component({
  selector: 'kpn-disk-space-used-chart',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <h2>Disk space used</h2>
    <div class="chart">
      <kpn-action-bar-chart
        [barChart]="barChart()"
        [xAxisLabel]="xAxisLabel()"
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
        [xAxisLabel]="xAxisLabel()"
        [yAxisLabel]="'bytes'"
        [legend]="false"
        [roundDomains]="false"
      />
    </div>
  `,
  standalone: true,
  imports: [ActionBarChartComponent, LineChartModule],
})
export class DiskSpaceUsedChartComponent {
  barChart = input.required<BarChart>();
  xAxisLabel = input.required<string>();
  view: [number, number] = [700, 300];

  data() {
    return [
      {
        name: 'Lesotho',
        series: this.barChart().data,
      },
    ];
  }
}
