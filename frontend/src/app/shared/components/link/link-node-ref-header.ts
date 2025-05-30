import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { input } from '@angular/core';
import { KnownElements } from '@api/common/common/known-elements';
import { Ref } from '@api/common/common/ref';
import { LinkNodeComponent } from './link-node.component';
import { OsmLinkNodeComponent } from './osm-link-node.component';

@Component({
  selector: 'ui-link-node-ref-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      <div class="kpn-thick">
        @if (known) {
          <ui-link-node [nodeId]="ref().id" [nodeName]="ref().name" />
        } @else {
          <span>{{ ref().name }}</span>
        }
      </div>
      <ui-osm-link-node [nodeId]="ref().id" [title]="ref().id.toString()" />
    </div>
  `,
  imports: [LinkNodeComponent, OsmLinkNodeComponent],
})
export class LinkNodeRefHeaderComponent implements OnInit {
  ref = input.required<Ref>();
  knownElements = input.required<KnownElements>();

  known: boolean;

  ngOnInit(): void {
    this.known = this.knownElements().nodeIds.includes(this.ref().id);
  }
}
