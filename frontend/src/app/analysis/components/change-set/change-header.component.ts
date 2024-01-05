import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { ChangeKey } from '@api/common/changes/details';
import { PageWidthService } from '@app/components/shared';
import { IconHappyComponent } from '@app/components/shared/icon';
import { IconInvestigateComponent } from '@app/components/shared/icon';
import { LinkChangesetComponent } from '@app/components/shared/link';
import { TimestampComponent } from '@app/components/shared/timestamp';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'kpn-change-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      @if (changeKey.changeSetId === 0) {
        <span i18n="@@change-header.start"> Start </span>
      }
      @if (changeKey.changeSetId > 0) {
        <kpn-link-changeset
          [changeSetId]="changeKey.changeSetId"
          [replicationNumber]="changeKey.replicationNumber"
          class="kpn-thick"
        />
      }
      @if (timestampOnSameLine$ | async) {
        <kpn-timestamp [timestamp]="changeKey.timestamp" class="kpn-thin" />
      }
      @if (happy) {
        <kpn-icon-happy />
      }
      @if (investigate) {
        <kpn-icon-investigate />
      }
    </div>
    @if (timestampOnSeparateLine$ | async) {
      <div>
        <kpn-timestamp [timestamp]="changeKey.timestamp" class="kpn-thin" />
      </div>
    }

    @if (comment) {
      <div class="comment">
        {{ comment }}
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
  imports: [
    AsyncPipe,
    IconHappyComponent,
    IconInvestigateComponent,
    LinkChangesetComponent,
    TimestampComponent,
  ],
})
export class ChangeHeaderComponent {
  @Input() changeKey: ChangeKey;
  @Input() happy: boolean;
  @Input() investigate: boolean;
  @Input() comment: string;

  private readonly pageWidthService = inject(PageWidthService);
  protected timestampOnSameLine$: Observable<boolean>;
  protected timestampOnSeparateLine$: Observable<boolean>;

  constructor() {
    this.timestampOnSeparateLine$ = this.pageWidthService.current$.pipe(
      map(() => this.timestampOnSeparateLine())
    );
    this.timestampOnSameLine$ = this.timestampOnSeparateLine$.pipe(map((value) => !value));
  }

  private timestampOnSeparateLine() {
    return (
      this.pageWidthService.isSmall() ||
      this.pageWidthService.isVerySmall() ||
      this.pageWidthService.isVeryVerySmall()
    );
  }
}
