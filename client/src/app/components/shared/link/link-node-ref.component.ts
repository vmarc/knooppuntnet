import {Component, Input, OnInit} from "@angular/core";
import {KnownElements} from "../../../kpn/shared/common/known-elements";
import {Ref} from "../../../kpn/shared/common/ref";

@Component({
  selector: "kpn-link-node-ref",
  template: `
    <kpn-link-node *ngIf="known" [nodeId]="ref.id" [nodeName]="ref.name"></kpn-link-node>
    <kpn-osm-link-node *ngIf="!known" [nodeId]="ref.id" [title]="ref.name"></kpn-osm-link-node>
  `
})
export class LinkNodeRefComponent implements OnInit {

  @Input() ref: Ref;
  @Input() knownElements: KnownElements;

  known: boolean;

  ngOnInit(): void {
    this.known = this.knownElements.nodeIds.contains(this.ref.id);
  }
}


