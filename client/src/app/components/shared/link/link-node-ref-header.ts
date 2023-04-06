import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit } from '@angular/core';
import { KnownElements } from '@api/common/common/known-elements';
import { Ref } from '@api/common/common/ref';

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
})
export class LinkNodeRefHeaderComponent implements OnInit {
  @Input() ref: Ref;
  @Input() knownElements: KnownElements;

  known: boolean;

  ngOnInit(): void {
    this.known = this.knownElements.nodeIds.includes(this.ref.id);
  }
}
