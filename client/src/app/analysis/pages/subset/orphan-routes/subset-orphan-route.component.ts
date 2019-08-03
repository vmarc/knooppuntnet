import {Component, Input, OnInit} from "@angular/core";
import {InterpretedTags} from "../../../../components/shared/tags/interpreted-tags";
import {Tags} from "../../../../kpn/shared/data/tags";

@Component({
  selector: "kpn-subset-orphan-route",
  template: `
    <p>
      <kpn-link-route [routeId]="route.id" [title]="route.name"></kpn-link-route>
    </p>
    <p>
      {{route.meters}}m
    </p>
    <p *ngIf="route.isBroken">
      route is broken
    </p>
    <p>
      {{route.nodes}}
    </p>
    <p>
      <kpn-timestamp [timestamp]="route.timestamp"></kpn-timestamp>
    </p>
    <p>
      <kpn-osm-link-relation [relationId]="route.id"></kpn-osm-link-relation>
      <kpn-josm-relation [relationId]="route.id"></kpn-josm-relation>
    </p>

    <p *ngIf="!extraTags.isEmpty()">
      <span>Extra tags:</span>
      <kpn-tags-table [tags]="extraTags"></kpn-tags-table>
    </p>
  `
})
export class SubsetOrphanRouteComponent implements OnInit {

  @Input() route;
  extraTags: InterpretedTags;

  ngOnInit(): void {
    this.extraTags = InterpretedTags.all(new Tags(InterpretedTags.routeTags(this.route.tags).extraTags()));
  }

}
