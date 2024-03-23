import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MonitorRouteChangeSummary } from '@api/common/monitor';
import { PageWidthService } from '@app/components/shared';
import { IconHappyComponent } from '@app/components/shared/icon';
import { IconInvestigateComponent } from '@app/components/shared/icon';

@Component({
  selector: 'kpn-monitor-change-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      <a [routerLink]="link()" class="kpn-thick">{{ changeSet().key.changeSetId }}</a>

      @if (timestampOnSameLine()) {
        <span class="kpn-thin">{{ changeSet().key.timestamp }}</span>
      }

      @if (changeSet().happy) {
        <kpn-icon-happy />
      }

      @if (changeSet().investigate) {
        <kpn-icon-investigate />
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
  standalone: true,
  imports: [IconHappyComponent, IconInvestigateComponent, RouterLink],
})
export class MonitorChangeHeaderComponent {
  changeSet = input.required<MonitorRouteChangeSummary>();

  private readonly pageWidthService = inject(PageWidthService);

  protected readonly timestampOnSeparateLine = computed(() => this.pageWidthService.isAllSmall());
  protected readonly timestampOnSameLine = computed(() => !this.timestampOnSeparateLine());

  link(): string {
    const key = this.changeSet().key;
    const groupName = this.changeSet().groupName;
    return `/monitor/groups/${groupName}/routes/${key.elementId}/changes/${key.changeSetId}/${key.replicationNumber}`;
  }
}
