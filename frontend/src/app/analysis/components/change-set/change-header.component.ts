import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ChangeKey } from '@api/common/changes/details/change-key';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';
import { IconInvestigateComponent } from '@app/shared/components/icon/icon-investigate.component';
import { LinkChangesetComponent } from '@app/shared/components/link/link-changeset.component';
import { PageWidthService } from '@app/shared/components/page-width.service';
import { TimestampComponent } from '@app/shared/components/timestamp/timestamp.component';

@Component({
  selector: 'kpn-change-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      @if (changeKey().changeSetId === 0) {
        <span i18n="@@change-header.start"> Start </span>
      }
      @if (changeKey().changeSetId > 0) {
        <kpn-link-changeset
          [changeSetId]="changeKey().changeSetId"
          [replicationNumber]="changeKey().replicationNumber"
          class="kpn-thick"
        />
      }
      @if (timestampOnSameLine()) {
        <kpn-timestamp [timestamp]="changeKey().timestamp" class="kpn-thin" />
      }
      @if (happy()) {
        <kpn-icon-happy />
      }
      @if (investigate()) {
        <kpn-icon-investigate />
      }
    </div>
    @if (timestampOnSeparateLine()) {
      <div>
        <kpn-timestamp [timestamp]="changeKey().timestamp" class="kpn-thin" />
      </div>
    }

    @if (comment()) {
      <div class="comment">
        {{ comment() }}
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
  imports: [
    IconHappyComponent,
    IconInvestigateComponent,
    LinkChangesetComponent,
    TimestampComponent,
  ],
})
export class ChangeHeaderComponent {
  changeKey = input.required<ChangeKey>();
  happy = input.required<boolean>();
  investigate = input.required<boolean>();
  comment = input.required<string>();

  private readonly pageWidthService = inject(PageWidthService);
  protected timestampOnSeparateLine = computed(() => this.pageWidthService.isAllSmall());
  protected timestampOnSameLine = computed(() => !this.timestampOnSeparateLine());
}
