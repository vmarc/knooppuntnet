import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Ref } from '@api/common/common';
import { LinkNodeComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-route-node-diff',
  changeDetection: ChangeDetectionStrategy.OnPush,

  template: `
    @switch (title()) {
      @case ('startNodes') {
        <span i18n="@@route-changes.node-diff.start-nodes">Start nodes</span>
      }
      @case ('endNodes') {
        <span i18n="@@route-changes.node-diff.end-nodes">End nodes</span>
      }
      @case ('startTentacleNodes') {
        <span i18n="@@route-changes.node-diff.start-tentacle-nodes">Start tentacle nodes</span>
      }
      @case ('endTentacleNodes') {
        <span i18n="@@route-changes.node-diff.end-tentacle-nodes">End tentacle nodes</span>
      }
    }

    <span>&nbsp;</span>

    @switch (action()) {
      @case ('added') {
        <span class="kpn-label" i18n="@@route-changes.node-diff.added">added</span>
      }
      @case ('removed') {
        <span class="kpn-label" i18n="@@route-changes.node-diff.removed">removed</span>
      }
    }

    <div class="kpn-comma-list">
      @for (node of nodeRefs(); track $index) {
        <kpn-link-node [nodeId]="node.id" [nodeName]="node.name" />
      }
    </div>
  `,
  standalone: true,
  imports: [LinkNodeComponent],
})
export class RouteNodeDiffComponent {
  title = input.required<string>(); // startNodes | endNodes | startTentacleNodes | endTentacleNodes
  action = input.required<string>(); // added | removed
  nodeRefs = input.required<Ref[]>();
}
