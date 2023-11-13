import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { DiskUsage } from '@api/common/status';
import { ServerDiskUsageLegendComponent } from './server-disk-usage-legend.component';
import { ServerDiskUsagePieChartComponent } from './server-disk-usage-pie-chart.component';

@Component({
  selector: 'kpn-server-disk-usage',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <div class="section">
      <p>Servers disk usage</p>
      <div class="charts">
        <kpn-server-disk-usage-pie-chart
          [data]="diskUsage.frontend.data"
          title="frontend"
          total="180G"
        />
        <kpn-server-disk-usage-pie-chart
          [data]="diskUsage.database.data"
          title="database"
          total="180G"
        />
        <kpn-server-disk-usage-pie-chart
          [data]="diskUsage.backend.data"
          title="backend"
          total="800G"
        />
        <kpn-server-disk-usage-legend />
      </div>
    </div>
  `,
  styles: [
    `
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
  ],
  standalone: true,
  imports: [ServerDiskUsagePieChartComponent, ServerDiskUsageLegendComponent],
})
export class ServerDiskUsageComponent {
  @Input({ required: true }) diskUsage: DiskUsage;
}
