import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details';
import { ChangeType } from '@api/custom';

@Component({
  selector: 'kpn-cs-nc-type',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (isCreate()) {
      <div class="kpn-detail">
        <b i18n="@@change-set.network-diffs.network-created">Network created</b>
      </div>
    }
    @if (isDelete()) {
      <div class="kpn-detail">
        <b i18n="@@change-set.network-diffs.network-deleted">Network deleted</b>
      </div>
    }
  `,
  standalone: true,
})
export class CsNcTypeComponent {
  networkChangeInfo = input.required<NetworkChangeInfo>();

  isCreate(): boolean {
    return this.networkChangeInfo().changeType === ChangeType.create;
  }

  isDelete(): boolean {
    return this.networkChangeInfo().changeType === ChangeType.delete;
  }
}
