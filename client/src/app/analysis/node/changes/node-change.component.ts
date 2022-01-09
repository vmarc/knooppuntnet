import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NodeChangeInfo } from '@api/common/node/node-change-info';

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

    <div *ngIf="nodeChangeInfo.changeKey.changeSetId === 0">
      <p i18n="@@node.initial-value">Oldest known state of node.</p>
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
}
