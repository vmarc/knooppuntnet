import {Component, Input} from '@angular/core';

@Component({
  selector: 'kpn-subset-orphan-route',
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
      <osm-link-relation relationId="{{route.id}}"></osm-link-relation>
      <josm-relation relationId="{{route.id}}"></josm-relation>
    </p>

    <p *ngIf="extraTags()">
      <span>Extra tags:</span>
      <tags [tags]="extraTags()"></tags>
    </p>
  `
})
export class SubsetOrphanRouteComponent {

  @Input() route;

  extraTags() {
    // TODO Tags(RouteTagFilter(route).extraTags)
    return this.route.tags;
  }

}
