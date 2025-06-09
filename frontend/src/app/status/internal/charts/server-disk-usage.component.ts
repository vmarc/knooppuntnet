import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { DiskUsage } from '@api/common/status/disk-usage';
import { ServerDiskUsageLegendComponent } from './server-disk-usage-legend.component';
import { ServerDiskUsagePieChartComponent } from './server-disk-usage-pie-chart.component';

@Component({
  selector: 'ui-server-disk-usage',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <div class="section">
      <p>Servers disk usage</p>
      <div class="charts">
        <ui-server-disk-usage-pie-chart
          [data]="diskUsage().frontend.data"
          title="frontend"
          total="180G"
        />
        <ui-server-disk-usage-pie-chart
          [data]="diskUsage().database.data"
          title="database"
          total="180G"
        />
        <ui-server-disk-usage-pie-chart
          [data]="diskUsage().backend.data"
          title="backend"
          total="800G"
        />
        <ui-server-disk-usage-legend />
      </div>
    </div>
  `,
  styles: `
    .section {
      margin-top: 20px;
      border-top: 1px solid lightgray;
      border-bottom: 1px solid lightgray;
    }

    .charts {
      display: flex;
      flex-wrap: wrap;
    }
  `,
  imports: [ServerDiskUsagePieChartComponent, ServerDiskUsageLegendComponent],
})
export class ServerDiskUsageComponent {
  readonly diskUsage = input.required<DiskUsage>();
}
