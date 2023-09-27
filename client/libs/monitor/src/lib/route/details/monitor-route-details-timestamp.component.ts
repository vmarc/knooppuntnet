import { NgIf } from '@angular/common';
import { OnInit } from '@angular/core';
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
        <span>{{ analysisDuration }}</span>
        <span i18n="@@monitor.route.details.seconds">seconds</span>
      </span>
    </p>
    <p *ngIf="!page.analysisTimestamp">-</p>
  `,
  standalone: true,
  imports: [NgIf, TimestampPipe, IntegerFormatPipe],
})
export class MonitorRouteDetailsTimestampComponent implements OnInit {
  @Input() page: MonitorRouteDetailsPage;

  analysisDuration = 0;

  ngOnInit(): void {
    this.analysisDuration = Math.round(this.page.analysisDuration / 1000);
  }
}
