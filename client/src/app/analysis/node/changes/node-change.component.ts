import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NodeChangeInfo } from '@api/common/node/node-change-info';
import { ChangeType } from '@api/custom/change-type';

@Component({
  selector: 'kpn-node-change',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-change-header
      [changeKey]="nodeChangeInfo.changeKey"
      [happy]="nodeChangeInfo.happy"
      [investigate]="nodeChangeInfo.investigate"
      [comment]="nodeChangeInfo.comment"
    >
    </kpn-change-header>

    <div *ngIf="isCreate()" class="kpn-detail">
      <b i18n="@@node-change.created">Node created</b>
    </div>
    <div *ngIf="isDelete()" class="kpn-detail">
      <b i18n="@@node-change.deleted">Node deleted</b>
    </div>

    <div *ngIf="nodeChangeInfo.changeKey.changeSetId === 0">
      <p i18n="@@node.initial-value">Oldest known state of the node.</p>
    </div>

    <kpn-change-set-tags
      [changeSetTags]="nodeChangeInfo.changeTags"
    ></kpn-change-set-tags>

    <div class="kpn-detail">
      <span i18n="@@node.version">Version</span>
      {{ nodeChangeInfo.version }}
      <span *ngIf="isVersionUnchanged()" i18n="@@node.unchanged"
        >(Unchanged)</span
      >
    </div>

    <kpn-node-change-detail
      [nodeChangeInfo]="nodeChangeInfo"
    ></kpn-node-change-detail>
  `,
})
export class NodeChangeComponent {
  @Input() nodeChangeInfo: NodeChangeInfo;

  isVersionUnchanged(): boolean {
    const before = this.nodeChangeInfo.before
      ? this.nodeChangeInfo.before.version
      : null;
    const after = this.nodeChangeInfo.after
      ? this.nodeChangeInfo.after.version
      : null;
    return before && after && before === after;
  }

  isCreate(): boolean {
    return this.nodeChangeInfo.changeType === ChangeType.create;
  }

  isDelete(): boolean {
    return this.nodeChangeInfo.changeType === ChangeType.delete;
  }
}
