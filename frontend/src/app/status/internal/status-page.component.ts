import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { PageComponent } from '@app/shared/components/page/page.component';
import { TimestampComponent } from '@app/shared/components/timestamp/timestamp.component';
import { ServerDiskUsageComponent } from './charts/server-disk-usage.component';
import { StatusLinksComponent } from './status-links.component';
import { StatusPageService } from './status-page.service';

@Component({
  selector: 'ui-status-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <ui-page>
      @if (service.response(); as response) {
        <p>
          <span class="kpn-label">The analysis is up-to-date until</span>
          <ui-timestamp [timestamp]="response.situationOn" />
        </p>
        <p>
          <span class="kpn-label">Replication details</span>
          <ui-status-links [links]="service.replicationLinks()" />
        </p>
        <p>
          <span class="kpn-label">System details</span>
          <ui-status-links [links]="service.systemLinks()" />
        </p>
        <p>
          <span class="kpn-label">Log analysis</span>
          <ui-status-links [links]="service.logLinks()" />
        </p>
        <ui-server-disk-usage [diskUsage]="response.result.diskUsage" />
      }
    </ui-page>
  `,
  providers: [StatusPageService],
  imports: [ServerDiskUsageComponent, StatusLinksComponent, TimestampComponent, PageComponent],
})
export class StatusPageComponent implements OnInit {
  readonly service = inject(StatusPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
