import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { RouteChangeInfo } from '@api/common/route/route-change-info';

@Component({
  selector: 'kpn-route-change',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-change-header
      [changeKey]="routeChangeInfo.changeKey"
      [happy]="routeChangeInfo.happy"
      [investigate]="routeChangeInfo.investigate"
      [comment]="routeChangeInfo.comment"
    >
    </kpn-change-header>

    <div *ngIf="routeChangeInfo.changeKey.changeSetId === 0">
      <p i18n="@@route-change.initial-value">
        Oldest known state of route (at date of
        <a
          class="external"
          href="https://wiki.openstreetmap.org/wiki/NL:Open_Database_License"
          target="_blank"
          rel="nofollow noreferrer"
          >license change</a
        >).
      </p>
    </div>

    <kpn-change-set-tags
      [changeSetTags]="routeChangeInfo.changeSetInfo?.tags"
    ></kpn-change-set-tags>

    <div class="kpn-detail">
      <span i18n="@@route-change.version">Version</span>
      {{ routeChangeInfo.version }}
      <span *ngIf="isVersionUnchanged()" i18n="@@route-change.unchanged"
        >(Unchanged)</span
      >
    </div>

    <kpn-route-change-detail
      [routeChangeInfo]="routeChangeInfo"
    ></kpn-route-change-detail>
  `,
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
}
