import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MonitorRouteChangeSummary } from '@api/common/monitor';
import { PageWidthService } from '@app/components/shared';
import { IconHappyComponent } from '@app/components/shared/icon';
import { IconInvestigateComponent } from '@app/components/shared/icon';
import { map } from 'rxjs/operators';

@Component({
  selector: 'kpn-monitor-change-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      <a [routerLink]="link()" class="kpn-thick">{{
        changeSet.key.changeSetId
      }}</a>
      <span *ngIf="timestampOnSameLine$ | async" class="kpn-thin">{{
        changeSet.key.timestamp
      }}</span>
      <kpn-icon-happy *ngIf="changeSet.happy" />
      <kpn-icon-investigate *ngIf="changeSet.investigate" />
    </div>
    <div *ngIf="timestampOnSeparateLine$ | async">
      <span class="kpn-thin">{{ changeSet.key.timestamp }}</span>
    </div>

    <div *ngIf="changeSet.comment" class="comment">
      {{ changeSet.comment }}
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
    RouterLink,
    NgIf,
    IconHappyComponent,
    IconInvestigateComponent,
    AsyncPipe,
  ],
})
export class MonitorChangeHeaderComponent {
  @Input() changeSet: MonitorRouteChangeSummary;

  readonly timestampOnSeparateLine$ = this.pageWidthService.current$.pipe(
    map(() => this.timestampOnSeparateLine())
  );
  readonly timestampOnSameLine$ = this.timestampOnSeparateLine$.pipe(
    map((value) => !value)
  );

  constructor(private pageWidthService: PageWidthService) {}

  link(): string {
    const key = this.changeSet.key;
    const groupName = this.changeSet.groupName;
    return `/monitor/groups/${groupName}/routes/${key.elementId}/changes/${key.changeSetId}/${key.replicationNumber}`;
  }

  private timestampOnSeparateLine() {
    return (
      this.pageWidthService.isSmall() ||
      this.pageWidthService.isVerySmall() ||
      this.pageWidthService.isVeryVerySmall()
    );
  }
}