import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { input } from '@angular/core';
import { KnownElements } from '@api/common/common/known-elements';
import { Ref } from '@api/common/common/ref';
import { LinkRouteComponent } from './link-route.component';
import { OsmLinkRelationComponent } from './osm-link-relation.component';

@Component({
  selector: 'kpn-link-route-ref-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      <div class="kpn-thick">
        @if (known) {
          <kpn-link-route [routeId]="ref().id" [routeName]="ref().name" />
        } @else {
          <span>{{ ref().name }}</span>
        }
      </div>
      <kpn-osm-link-relation [relationId]="ref().id" [title]="ref().id.toString()" />
    </div>
  `,
  imports: [LinkRouteComponent, OsmLinkRelationComponent],
})
export class LinkRouteRefHeaderComponent implements OnInit {
  ref = input.required<Ref>();
  knownElements = input.required<KnownElements>();

  known: boolean;

  ngOnInit(): void {
    this.known = this.knownElements().routeIds.includes(this.ref().id);
  }
}
