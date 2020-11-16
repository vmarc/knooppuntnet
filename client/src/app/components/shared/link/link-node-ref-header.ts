import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input, OnInit} from '@angular/core';
import {KnownElements} from '../../../kpn/api/common/common/known-elements';
import {Ref} from '../../../kpn/api/common/common/ref';

@Component({
  selector: 'kpn-link-node-ref-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      <div class="kpn-thick">
        <kpn-link-node *ngIf="known" [nodeId]="ref.id" [nodeName]="ref.name"></kpn-link-node>
        <span *ngIf="!known">{{ref.name}}</span>
      </div>
      <kpn-osm-link-node [nodeId]="ref.id" [title]="ref.id.toString()"></kpn-osm-link-node>
    </div>
  `
})
export class LinkNodeRefHeaderComponent implements OnInit {

  @Input() ref: Ref;
  @Input() knownElements: KnownElements;

  known: boolean;

  ngOnInit(): void {
    this.known = this.knownElements.nodeIds.contains(this.ref.id);
  }
}
