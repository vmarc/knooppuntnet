import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { Status } from '@api/common/status/status';
import { ApiResponse } from '@api/custom/api-response';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { AppService } from '../../app.service';
import { StatusLinks } from './status-links';

/* tslint:disable:template-i18n English only */
@Component({
  selector: 'kpn-status-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="response$ | async as response">
      <p>
        <span class="kpn-label">The analysis is up-to-date until</span>
        <kpn-timestamp [timestamp]="response.situationOn"></kpn-timestamp>
      </p>
      <p>
        <span class="kpn-label">Replication details</span>
        <kpn-status-links [links]="replicationLinks"></kpn-status-links>
      </p>
      <p>
        <span class="kpn-label">System details</span>
        <kpn-status-links [links]="systemLinks"></kpn-status-links>
      </p>
      <p>
        <span class="kpn-label">Log analysis</span>
        <kpn-status-links [links]="logLinks"></kpn-status-links>
      </p>
      <kpn-server-disk-usage
        [diskUsage]="response.result.diskUsage"
      ></kpn-server-disk-usage>
    </div>
  `,
})
export class StatusPageComponent implements OnInit {
  response$: Observable<ApiResponse<Status>>;

  replicationLinks: StatusLinks;
  systemLinks: StatusLinks;
  logLinks: StatusLinks;

  constructor(private readonly appService: AppService) {}

  ngOnInit(): void {
    this.response$ = this.appService.status().pipe(
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
