import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { Status } from '@api/common/status';
import { ApiResponse } from '@api/custom';
import { ApiService } from '@app/services';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { StatusLinks } from './status-links';

@Component({
  selector: 'kpn-status-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <div *ngIf="response$ | async as response">
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
    </div>
  `,
})
export class StatusPageComponent implements OnInit {
  response$: Observable<ApiResponse<Status>>;

  replicationLinks: StatusLinks;
  systemLinks: StatusLinks;
  logLinks: StatusLinks;

  constructor(private readonly apiService: ApiService) {}

  ngOnInit(): void {
    this.response$ = this.apiService.status().pipe(
      tap((response) => {
        if (response.result) {
          const timestamp = response.result.timestamp;
          this.replicationLinks = new StatusLinks(
            timestamp,
            '/status/replication'
          );
          this.systemLinks = new StatusLinks(timestamp, '/status/system');
          this.logLinks = new StatusLinks(timestamp, '/status/log');
        }
      })
    );
  }
}
