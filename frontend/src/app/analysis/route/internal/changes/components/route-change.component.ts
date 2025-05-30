import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteChangeInfo } from '@api/common/route/route-change-info';
import { ChangeHeaderComponent } from '@app/analysis/components/change-set/change-header.component';
import { ChangeSetTagsComponent } from '@app/analysis/components/change-set/change-set-tags.component';
import { RouteChangeDetailComponent } from '@app/analysis/components/changes/route/route-change-detail.component';

@Component({
  selector: 'ui-route-change',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-change-header
      [changeKey]="routeChangeInfo().changeKey"
      [happy]="routeChangeInfo().happy"
      [investigate]="routeChangeInfo().investigate"
      [comment]="routeChangeInfo().comment"
    />

    @if (isCreate()) {
      <div class="kpn-detail">
        <b i18n="@@route-change.created">Route created</b>
      </div>
    }
    @if (isDelete()) {
      <div class="kpn-detail">
        <b i18n="@@route-change.deleted">Route deleted</b>
      </div>
    }

    @if (routeChangeInfo().changeKey.changeSetId === 0) {
      <p i18n="@@route-change.initial-value">Oldest known state of the route.</p>
    }

    <ui-change-set-tags [changeSetTags]="routeChangeInfo().changeSetInfo?.tags" />

    <div class="kpn-detail">
      <span i18n="@@route-change.version">Version</span>
      {{ routeChangeInfo().version }}
      @if (isVersionUnchanged()) {
        <span i18n="@@route-change.unchanged">(Unchanged)</span>
      }
    </div>

    <ui-route-change-detail [routeChangeInfo]="routeChangeInfo()" />
  `,
  imports: [ChangeHeaderComponent, ChangeSetTagsComponent, RouteChangeDetailComponent],
})
export class RouteChangeComponent {
  routeChangeInfo = input.required<RouteChangeInfo>();

  isVersionUnchanged(): boolean {
    const before = this.routeChangeInfo().before ? this.routeChangeInfo().before.version : null;
    const after = this.routeChangeInfo().after ? this.routeChangeInfo().after.version : null;
    return before && after && before === after;
  }

  isCreate(): boolean {
    return this.routeChangeInfo().changeType === 'create';
  }

  isDelete(): boolean {
    return this.routeChangeInfo().changeType === 'delete';
  }
}
