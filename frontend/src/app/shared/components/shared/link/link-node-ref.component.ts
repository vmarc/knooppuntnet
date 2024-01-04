import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit } from '@angular/core';
import { KnownElements } from '@api/common/common';
import { Ref } from '@api/common/common';
import { LinkNodeComponent } from './link-node.component';
import { OsmLinkNodeComponent } from './osm-link-node.component';

@Component({
  selector: 'kpn-link-node-ref',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (known) {
      <kpn-link-node [nodeId]="ref.id" [nodeName]="ref.name" />
    } @else {
      <kpn-osm-link-node [nodeId]="ref.id" [title]="ref.name" />
    }
  `,
  standalone: true,
  imports: [LinkNodeComponent, OsmLinkNodeComponent],
})
export class LinkNodeRefComponent implements OnInit {
  @Input({ required: true }) ref: Ref;
  @Input({ required: true }) knownElements: KnownElements;

  known: boolean;

  ngOnInit(): void {
    this.known = this.knownElements.nodeIds.includes(this.ref.id);
  }
}
