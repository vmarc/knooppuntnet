import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { RouteChangeInfo } from '@api/common/route';
import { ChangeType } from '@api/custom';
import { ChangeHeaderComponent } from '@app/analysis/components/change-set';
import { ChangeSetTagsComponent } from '@app/analysis/components/change-set';
import { RouteChangeDetailComponent } from '@app/analysis/components/changes/route';

@Component({
  selector: 'kpn-route-change',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-change-header
      [changeKey]="routeChangeInfo.changeKey"
      [happy]="routeChangeInfo.happy"
      [investigate]="routeChangeInfo.investigate"
      [comment]="routeChangeInfo.comment"
    />

    <div *ngIf="isCreate()" class="kpn-detail">
      <b i18n="@@route-change.created">Route created</b>
    </div>
    <div *ngIf="isDelete()" class="kpn-detail">
      <b i18n="@@route-change.deleted">Route deleted</b>
    </div>

    <div *ngIf="routeChangeInfo.changeKey.changeSetId === 0">
      <p i18n="@@route-change.initial-value">
        Oldest known state of the route.
      </p>
    </div>

    <kpn-change-set-tags
      [changeSetTags]="routeChangeInfo.changeSetInfo?.tags"
    />

    <div class="kpn-detail">
      <span i18n="@@route-change.version">Version</span>
      {{ routeChangeInfo.version }}
      <span *ngIf="isVersionUnchanged()" i18n="@@route-change.unchanged"
        >(Unchanged)</span
      >
    </div>

    <kpn-route-change-detail [routeChangeInfo]="routeChangeInfo" />
  `,
  standalone: true,
  imports: [
    ChangeHeaderComponent,
    ChangeSetTagsComponent,
    NgIf,
    RouteChangeDetailComponent,
  ],
})
export class RouteChangeComponent {
  @Input() routeChangeInfo: RouteChangeInfo;

  isVersionUnchanged(): boolean {
    const before = this.routeChangeInfo.before
      ? this.routeChangeInfo.before.version
      : null;
    const after = this.routeChangeInfo.after
      ? this.routeChangeInfo.after.version
      : null;
    return before && after && before === after;
  }

  isCreate(): boolean {
    return this.routeChangeInfo.changeType === ChangeType.create;
  }

  isDelete(): boolean {
    return this.routeChangeInfo.changeType === ChangeType.delete;
  }
}
