import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit } from '@angular/core';
import { InterpretedTags } from '../../../components/shared/tags/interpreted-tags';
import { RouteSummary } from '@api/common/route-summary';
import { Tags } from '@api/custom/tags';

@Component({
  selector: 'kpn-subset-orphan-route',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p>
      <kpn-link-route
        [routeId]="route.id"
        [title]="route.name"
      ></kpn-link-route>
    </p>
    <p class="kpn-meters">
      {{ route.meters }}
    </p>
    <p *ngIf="route.isBroken" i18n="@@subset-orphan-routes.route-is-broken">
      route is broken
    </p>
    <p *ngIf="!route.nodeNames.isEmpty()" class="kpn-comma-list">
      <span *ngFor="let name of route.nodeNames">
        {{ name }}
      </span>
    </p>
    <p>
      <kpn-timestamp [timestamp]="route.timestamp"></kpn-timestamp>
    </p>
    <p>
      <kpn-osm-link-relation [relationId]="route.id"></kpn-osm-link-relation>
      <kpn-josm-relation [relationId]="route.id"></kpn-josm-relation>
    </p>

    <p *ngIf="!extraTags.isEmpty()">
      <span i18n="@@subset-orphan-routes.extra-tags">Extra tags:</span>
      <kpn-tags-table [tags]="extraTags"></kpn-tags-table>
    </p>
  `,
})
export class SubsetOrphanRouteComponent implements OnInit {
  @Input() route: RouteSummary;
  extraTags: InterpretedTags;

  ngOnInit(): void {
    this.extraTags = InterpretedTags.all(
      new Tags(InterpretedTags.routeTags(this.route.tags).extraTags())
    );
  }
}
