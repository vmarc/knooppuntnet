import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { OldPageComponent } from '@app/components/shared/page';
import { TimestampComponent } from '@app/components/shared/timestamp';
import { ServerDiskUsageComponent } from './charts/server-disk-usage.component';
import { StatusLinksComponent } from './status-links.component';
import { StatusPageService } from './status-page.service';
import { StatusSidebarComponent } from './status-sidebar.component';

@Component({
  selector: 'kpn-status-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <kpn-old-page>
      @if (service.response(); as response) {
        <p>
          <span class="kpn-label">The analysis is up-to-date until</span>
          <kpn-timestamp [timestamp]="response.situationOn" />
        </p>
        <p>
          <span class="kpn-label">Replication details</span>
          <kpn-status-links [links]="service.replicationLinks()" />
        </p>
        <p>
          <span class="kpn-label">System details</span>
          <kpn-status-links [links]="service.systemLinks()" />
        </p>
        <p>
          <span class="kpn-label">Log analysis</span>
          <kpn-status-links [links]="service.logLinks()" />
        </p>
        <kpn-server-disk-usage [diskUsage]="response.result.diskUsage" />
      }
      <kpn-status-sidebar sidebar />
    </kpn-old-page>
  `,
  providers: [StatusPageService],
  imports: [
    OldPageComponent,
    ServerDiskUsageComponent,
    StatusLinksComponent,
    StatusSidebarComponent,
    TimestampComponent,
  ],
})
export class StatusPageComponent implements OnInit {
  readonly service = inject(StatusPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
