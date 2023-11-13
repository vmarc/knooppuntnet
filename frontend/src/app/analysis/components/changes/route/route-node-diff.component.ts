import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { Ref } from '@api/common/common';
import { LinkNodeComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-route-node-diff',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <span
      *ngIf="title === 'startNodes'"
      i18n="@@route-changes.node-diff.start-nodes"
      >Start nodes</span
    >
    <span
      *ngIf="title === 'endNodes'"
      i18n="@@route-changes.node-diff.end-nodes"
      >End nodes</span
    >
    <span
      *ngIf="title === 'startTentacleNodes'"
      i18n="@@route-changes.node-diff.start-tentacle-nodes"
      >Start tentacle nodes</span
    >
    <span
      *ngIf="title === 'endTentacleNodes'"
      i18n="@@route-changes.node-diff.end-tentacle-nodes"
      >End tentacle nodes</span
    >

    <span>&nbsp;</span>

    <span
      *ngIf="action === 'added'"
      class="kpn-label"
      i18n="@@route-changes.node-diff.added"
      >added</span
    >
    <span
      *ngIf="action === 'removed'"
      class="kpn-label"
      i18n="@@route-changes.node-diff.removed"
      >removed</span
    >

    <div class="kpn-comma-list">
      <kpn-link-node
        *ngFor="let node of nodeRefs"
        [nodeId]="node.id"
        [nodeName]="node.name"
      />
    </div>
  `,
  standalone: true,
  imports: [NgIf, NgFor, LinkNodeComponent],
})
export class RouteNodeDiffComponent {
  @Input() title: string; // startNodes | endNodes | startTentacleNodes | endTentacleNodes
  @Input() action: string; // added | removed
  @Input() nodeRefs: Ref[];
}
