import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details';
import { ChangeType } from '@api/custom';

@Component({
  selector: 'kpn-cs-nc-type',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="isCreate()" class="kpn-detail">
      <b i18n="@@change-set.network-diffs.network-created">Network created</b>
    </div>
    <div *ngIf="isDelete()" class="kpn-detail">
      <b i18n="@@change-set.network-diffs.network-deleted">Network deleted</b>
    </div>
  `,
  standalone: true,
  imports: [NgIf],
})
export class CsNcTypeComponent {
  @Input() networkChangeInfo: NetworkChangeInfo;

  isCreate(): boolean {
    return this.networkChangeInfo.changeType === ChangeType.create;
  }

  isDelete(): boolean {
    return this.networkChangeInfo.changeType === ChangeType.delete;
  }
}
