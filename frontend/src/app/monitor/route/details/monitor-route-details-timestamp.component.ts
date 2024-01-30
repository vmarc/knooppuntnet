import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MonitorRouteDetailsPage } from '@api/common/monitor';
import { IntegerFormatPipe } from '@app/components/shared/format';
import { TimestampPipe } from '@app/components/shared/format';

@Component({
  selector: 'kpn-monitor-route-details-timestamp',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (page().analysisTimestamp) {
      <p class="kpn-line">
        <span>{{ page().analysisTimestamp | yyyymmddhhmm }}</span>
        <span class="kpn-brackets">
          <span>{{ analysisDuration }}</span
          >&nbsp;<span i18n="@@monitor.route.details.seconds">seconds</span>
        </span>
      </p>
    } @else {
      -
    }
  `,
  standalone: true,
  imports: [TimestampPipe, IntegerFormatPipe],
})
export class MonitorRouteDetailsTimestampComponent implements OnInit {
  page = input.required<MonitorRouteDetailsPage>();

  analysisDuration = 0;

  ngOnInit(): void {
    this.analysisDuration = Math.round(this.page().analysisDuration / 1000);
  }
}
