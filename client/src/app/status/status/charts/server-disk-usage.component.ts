import {ChangeDetectionStrategy} from '@angular/core';
import {Input} from '@angular/core';
import {Component} from '@angular/core';
import {DiskUsage} from '@api/common/status/disk-usage';

/* tslint:disable:template-i18n English only */
@Component({
  selector: 'kpn-server-disk-usage',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="section">
      <p>Servers disk usage</p>
      <div class="charts">
        <kpn-server-disk-usage-pie-chart [data]="diskUsage.frontend.data" title="frontend" total="180G"></kpn-server-disk-usage-pie-chart>
        <kpn-server-disk-usage-pie-chart [data]="diskUsage.database.data" title="database" total="180G"></kpn-server-disk-usage-pie-chart>
        <kpn-server-disk-usage-pie-chart [data]="diskUsage.backend.data" title="backend" total="800G"></kpn-server-disk-usage-pie-chart>
        <kpn-server-disk-usage-legend></kpn-server-disk-usage-legend>
      </div>
    </div>
  `,
  styles: [`

    .section {
      margin-top: 20px;
      border-top: 1px solid lightgray;
      border-bottom: 1px solid lightgray;
    }

    .charts {
      display: flex;
      flex-wrap: wrap;
    }

  `]
})
export class ServerDiskUsageComponent {
  @Input() diskUsage: DiskUsage;
}
