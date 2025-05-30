import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MonitorRouteChangeSummary } from '@api/common/monitor/monitor-route-change-summary';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';
import { IconInvestigateComponent } from '@app/shared/components/icon/icon-investigate.component';
import { PageWidthService } from '@app/shared/components/page-width.service';

@Component({
  selector: 'ui-monitor-change-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      <a [routerLink]="link()" class="kpn-thick">{{ changeSet().key.changeSetId }}</a>

      @if (timestampOnSameLine()) {
        <span class="kpn-thin">{{ changeSet().key.timestamp }}</span>
      }

      @if (changeSet().happy) {
        <ui-icon-happy />
      }

      @if (changeSet().investigate) {
        <ui-icon-investigate />
      }
    </div>

    @if (timestampOnSeparateLine()) {
      <div>
        <span class="kpn-thin">{{ changeSet().key.timestamp }}</span>
      </div>
    }

    @if (changeSet().comment) {
      <div class="comment">
        {{ changeSet().comment }}
      </div>
    }
  `,
  styles: `
    .comment {
      padding-top: 5px;
      padding-bottom: 5px;
      font-style: italic;
    }
  `,
  imports: [IconHappyComponent, IconInvestigateComponent, RouterLink],
})
export class MonitorChangeHeaderComponent {
  changeSet = input.required<MonitorRouteChangeSummary>();

  private readonly pageWidthService = inject(PageWidthService);

  readonly timestampOnSeparateLine = computed(() => this.pageWidthService.isAllSmall());
  readonly timestampOnSameLine = computed(() => !this.timestampOnSeparateLine());

  link(): string {
    const key = this.changeSet().key;
    const groupName = this.changeSet().groupName;
    return `/monitor/groups/${groupName}/routes/${key.elementId}/changes/${key.changeSetId}/${key.replicationNumber}`;
  }
}
