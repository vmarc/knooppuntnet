import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit } from '@angular/core';
import { KnownElements } from '@api/common/common/known-elements';
import { Ref } from '@api/common/common/ref';

@Component({
  selector: 'kpn-link-node-ref',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-link-node
      *ngIf="known"
      [nodeId]="ref.id"
      [nodeName]="ref.name"
    />
    <kpn-osm-link-node
      *ngIf="!known"
      [nodeId]="ref.id"
      [title]="ref.name"
    />
  `,
})
export class LinkNodeRefComponent implements OnInit {
  @Input() ref: Ref;
  @Input() knownElements: KnownElements;

  known: boolean;

  ngOnInit(): void {
    this.known = this.knownElements.nodeIds.includes(this.ref.id);
  }
}
