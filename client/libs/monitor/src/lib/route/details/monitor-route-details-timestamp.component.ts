import { NgIf } from '@angular/common';
import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteDetailsPage } from '@api/common/monitor';
import { IntegerFormatPipe } from '@app/components/shared/format';
import { TimestampPipe } from '@app/components/shared/format';

@Component({
  selector: 'kpn-monitor-route-details-timestamp',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p *ngIf="page.analysisTimestamp" class="kpn-line">
      <span>{{ page.analysisTimestamp | yyyymmddhhmm }}</span>
      <span class="kpn-brackets">
        <span class="kpn-label" i18n="@@monitor.route.details.duration"
          >duration</span
        >
        <span class="kpn-milliseconds">
          {{ page.analysisDuration | integer }}
        </span>
      </span>
    </p>
    <p *ngIf="!page.analysisTimestamp">-</p>
  `,
  standalone: true,
  imports: [NgIf, TimestampPipe, IntegerFormatPipe],
})
export class MonitorRouteDetailsTimestampComponent {
  @Input() page: MonitorRouteDetailsPage;
}
