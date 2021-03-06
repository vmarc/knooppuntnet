import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { BarChart } from '@api/common/status/bar-chart';

/* tslint:disable:template-i18n English only */
@Component({
  selector: 'kpn-disk-space-overpass-chart',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <h2>Disk space overpass</h2>
    <div class="chart">
      <kpn-action-bar-chart
        [barChart]="barChart"
        [xAxisLabel]="xAxisLabel"
        yAxisLabel="TODO bytes?"
      >
      </kpn-action-bar-chart>
    </div>
  `,
})
export class DiskSpaceOverpassChartComponent {
  @Input() barChart: BarChart;
  @Input() xAxisLabel: string;
}
