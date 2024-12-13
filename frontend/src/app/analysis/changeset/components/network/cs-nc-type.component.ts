import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details';

@Component({
  selector: 'kpn-cs-nc-type',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (changeType() === 'create') {
      <div class="kpn-detail">
        <b i18n="@@change-set.network-diffs.network-created">Network created</b>
      </div>
    }
    @if (changeType() === 'delete') {
      <div class="kpn-detail">
        <b i18n="@@change-set.network-diffs.network-deleted">Network deleted</b>
      </div>
    }
  `,
  standalone: true,
})
export class CsNcTypeComponent {
  readonly networkChangeInfo = input.required<NetworkChangeInfo>();
  readonly changeType = computed(() => this.networkChangeInfo().changeType);
}
