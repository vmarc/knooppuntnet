import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input, OnInit} from "@angular/core";
import {KnownElements} from "../../../kpn/api/common/common/known-elements";
import {Ref} from "../../../kpn/api/common/common/ref";

@Component({
  selector: "kpn-link-node-ref-header",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line" *ngIf="known">
      <div class="kpn-thick">
        <kpn-link-node [nodeId]="ref.id" [nodeName]="ref.name"></kpn-link-node>
      </div>
      <kpn-osm-link-node [nodeId]="ref.id" [title]="ref.name"></kpn-osm-link-node>
    </div>
    <div class="kpn-line" *ngIf="!known">
      <div class="kpn-thick">
        <kpn-osm-link-node [nodeId]="ref.id" [title]="ref.name"></kpn-osm-link-node>
      </div>
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

