import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { input } from '@angular/core';
import { KnownElements } from '@api/common/common';
import { Ref } from '@api/common/common';
import { LinkRouteComponent } from './link-route.component';
import { OsmLinkRelationComponent } from './osm-link-relation.component';

@Component({
  selector: 'kpn-link-route-ref',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (known) {
      <kpn-link-route [routeId]="ref().id" [routeName]="ref().name" />
    } @else {
      <kpn-osm-link-relation [relationId]="ref().id" [title]="ref().name" />
    }
  `,
  standalone: true,
  imports: [LinkRouteComponent, OsmLinkRelationComponent],
})
export class LinkRouteRefComponent implements OnInit {
  ref = input.required<Ref>();
  knownElements = input.required<KnownElements>();

  known: boolean;

  ngOnInit(): void {
    this.known = this.knownElements().routeIds.includes(this.ref().id);
  }
}
