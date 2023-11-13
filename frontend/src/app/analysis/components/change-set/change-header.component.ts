import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
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
      <span *ngIf="changeKey.changeSetId === 0" i18n="@@change-header.start">
        Start
      </span>
      <kpn-link-changeset
        *ngIf="changeKey.changeSetId > 0"
        [changeSetId]="changeKey.changeSetId"
        [replicationNumber]="changeKey.replicationNumber"
        class="kpn-thick"
      />
      <kpn-timestamp
        *ngIf="timestampOnSameLine$ | async"
        [timestamp]="changeKey.timestamp"
        class="kpn-thin"
      />
      <kpn-icon-happy *ngIf="happy" />
      <kpn-icon-investigate *ngIf="investigate" />
    </div>
    <div *ngIf="timestampOnSeparateLine$ | async">
      <kpn-timestamp [timestamp]="changeKey.timestamp" class="kpn-thin" />
    </div>

    <div *ngIf="comment" class="comment">
      {{ comment }}
    </div>
  `,
  styles: [
    `
      .comment {
        padding-top: 5px;
        padding-bottom: 5px;
        font-style: italic;
      }
    `,
  ],
  standalone: true,
  imports: [
    AsyncPipe,
    IconHappyComponent,
    IconInvestigateComponent,
    LinkChangesetComponent,
    NgIf,
    TimestampComponent,
  ],
})
export class ChangeHeaderComponent {
  @Input() changeKey: ChangeKey;
  @Input() happy: boolean;
  @Input() investigate: boolean;
  @Input() comment: string;

  timestampOnSameLine$: Observable<boolean>;
  timestampOnSeparateLine$: Observable<boolean>;

  constructor(private pageWidthService: PageWidthService) {
    this.timestampOnSeparateLine$ = this.pageWidthService.current$.pipe(
      map(() => this.timestampOnSeparateLine())
    );
    this.timestampOnSameLine$ = this.timestampOnSeparateLine$.pipe(
      map((value) => !value)
    );
  }

  private timestampOnSeparateLine() {
    return (
      this.pageWidthService.isSmall() ||
      this.pageWidthService.isVerySmall() ||
      this.pageWidthService.isVeryVerySmall()
    );
  }
}
