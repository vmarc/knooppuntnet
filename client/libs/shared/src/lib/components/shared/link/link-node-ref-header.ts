import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { OnInit } from '@angular/core';
import { KnownElements } from '@api/common/common';
import { Ref } from '@api/common/common';
import { LinkNodeComponent } from './link-node.component';
import { OsmLinkNodeComponent } from './osm-link-node.component';

@Component({
  selector: 'kpn-link-node-ref-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      <div class="kpn-thick">
        <kpn-link-node *ngIf="known" [nodeId]="ref.id" [nodeName]="ref.name" />
        <span *ngIf="!known">{{ ref.name }}</span>
      </div>
      <kpn-osm-link-node [nodeId]="ref.id" [title]="ref.id.toString()" />
    </div>
  `,
  standalone: true,
  imports: [NgIf, LinkNodeComponent, OsmLinkNodeComponent],
})
export class LinkNodeRefHeaderComponent implements OnInit {
  @Input({ required: true }) ref: Ref;
  @Input({ required: true }) knownElements: KnownElements;

  known: boolean;

  ngOnInit(): void {
    this.known = this.knownElements.nodeIds.includes(this.ref.id);
  }
}
