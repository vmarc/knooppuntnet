import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { BarChart } from '@api/common/status';

@Component({
  selector: 'kpn-disk-space-overpass-chart',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <h2>Disk space overpass</h2>
    <div class="chart">
      <kpn-action-bar-chart
        [barChart]="barChart"
        [xAxisLabel]="xAxisLabel"
        yAxisLabel="TODO bytes?"
      />
    </div>
  `,
})
export class DiskSpaceOverpassChartComponent {
  @Input() barChart: BarChart;
  @Input() xAxisLabel: string;
}
