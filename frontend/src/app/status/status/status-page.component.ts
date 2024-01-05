import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { Status } from '@api/common/status';
import { ApiResponse } from '@api/custom';
import { PageComponent } from '@app/components/shared/page';
import { TimestampComponent } from '@app/components/shared/timestamp';
import { ApiService } from '@app/services';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { ServerDiskUsageComponent } from './charts/server-disk-usage.component';
import { StatusLinks } from './status-links';
import { StatusLinksComponent } from './status-links.component';
import { StatusSidebarComponent } from './status-sidebar.component';

@Component({
  selector: 'kpn-status-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <kpn-page>
      @if (response$ | async; as response) {
        <p>
          <span class="kpn-label">The analysis is up-to-date until</span>
          <kpn-timestamp [timestamp]="response.situationOn" />
        </p>
        <p>
          <span class="kpn-label">Replication details</span>
          <kpn-status-links [links]="replicationLinks" />
        </p>
        <p>
          <span class="kpn-label">System details</span>
          <kpn-status-links [links]="systemLinks" />
        </p>
        <p>
          <span class="kpn-label">Log analysis</span>
          <kpn-status-links [links]="logLinks" />
        </p>
        <kpn-server-disk-usage [diskUsage]="response.result.diskUsage" />
      }
      <kpn-status-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    AsyncPipe,
    PageComponent,
    ServerDiskUsageComponent,
    StatusLinksComponent,
    StatusSidebarComponent,
    TimestampComponent,
  ],
})
export class StatusPageComponent implements OnInit {
  private readonly apiService = inject(ApiService);

  protected response$: Observable<ApiResponse<Status>>;
  protected replicationLinks: StatusLinks;
  protected systemLinks: StatusLinks;
  protected logLinks: StatusLinks;

  ngOnInit(): void {
    this.response$ = this.apiService.status().pipe(
      tap((response) => {
        if (response.result) {
          const timestamp = response.result.timestamp;
          this.replicationLinks = new StatusLinks(timestamp, '/status/replication');
          this.systemLinks = new StatusLinks(timestamp, '/status/system');
          this.logLinks = new StatusLinks(timestamp, '/status/log');
        }
      })
    );
  }
}
