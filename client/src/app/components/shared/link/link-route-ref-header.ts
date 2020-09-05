import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input, OnInit} from "@angular/core";
import {KnownElements} from "../../../kpn/api/common/common/known-elements";
import {Ref} from "../../../kpn/api/common/common/ref";

@Component({
  selector: "kpn-link-route-ref-header",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line" *ngIf="known">
      <div class="kpn-thick">
        <kpn-link-route [routeId]="ref.id" [title]="ref.name"></kpn-link-route>
      </div>
      <kpn-osm-link-relation [relationId]="ref.id" [title]="ref.name"></kpn-osm-link-relation>
    </div>
    <div class="kpn-line" *ngIf="!known">
      <div class="kpn-thick">
        <kpn-link-route [routeId]="ref.id" [title]="ref.name"></kpn-link-route>
      </div>
    </div>
  `
})
export class LinkRouteRefHeaderComponent implements OnInit {

  @Input() ref: Ref;
  @Input() knownElements: KnownElements;

  known: boolean;

  ngOnInit(): void {
    this.known = this.knownElements.routeIds.contains(this.ref.id);
  }
}
