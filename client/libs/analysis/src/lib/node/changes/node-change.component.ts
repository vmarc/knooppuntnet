import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { NodeChangeInfo } from '@api/common/node';
import { ChangeType } from '@api/custom';
import { ChangeHeaderComponent } from '@app/analysis/components/change-set';
import { ChangeSetTagsComponent } from '@app/analysis/components/change-set';
import { NodeChangeDetailComponent } from '@app/analysis/components/changes/node';

@Component({
  selector: 'kpn-node-change',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-change-header
      [changeKey]="nodeChangeInfo.changeKey"
      [happy]="nodeChangeInfo.happy"
      [investigate]="nodeChangeInfo.investigate"
      [comment]="nodeChangeInfo.comment"
    />

    <div *ngIf="isCreate()" class="kpn-detail">
      <b i18n="@@node-change.created">Node created</b>
    </div>
    <div *ngIf="isDelete()" class="kpn-detail">
      <b i18n="@@node-change.deleted">Node deleted</b>
    </div>

    <div *ngIf="nodeChangeInfo.changeKey.changeSetId === 0">
      <p i18n="@@node.initial-value">Oldest known state of the node.</p>
    </div>

    <kpn-change-set-tags [changeSetTags]="nodeChangeInfo.changeTags" />

    <div class="kpn-detail">
      <span i18n="@@node.version">Version</span>
      {{ nodeChangeInfo.version }}
      <span *ngIf="isVersionUnchanged()" i18n="@@node.unchanged"
        >(Unchanged)</span
      >
    </div>

    <kpn-node-change-detail [nodeChangeInfo]="nodeChangeInfo" />
  `,
  standalone: true,
  imports: [
    ChangeHeaderComponent,
    ChangeSetTagsComponent,
    NgIf,
    NodeChangeDetailComponent,
  ],
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
