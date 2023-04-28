import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { OnInit } from '@angular/core';
import { KnownElements } from '@api/common/common';
import { Ref } from '@api/common/common';
import { LinkRouteComponent } from './link-route.component';
import { OsmLinkRelationComponent } from './osm-link-relation.component';

@Component({
  selector: 'kpn-link-route-ref-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      <div class="kpn-thick">
        <kpn-link-route
          *ngIf="known"
          [routeId]="ref.id"
          [routeName]="ref.name"
        ></kpn-link-route>
        <span *ngIf="!known">{{ ref.name }}</span>
      </div>
      <kpn-osm-link-relation
        [relationId]="ref.id"
        [title]="ref.id.toString()"
      ></kpn-osm-link-relation>
    </div>
  `,
  standalone: true,
  imports: [NgIf, LinkRouteComponent, OsmLinkRelationComponent],
})
export class LinkRouteRefHeaderComponent implements OnInit {
  @Input() ref: Ref;
  @Input() knownElements: KnownElements;

  known: boolean;

  ngOnInit(): void {
    this.known = this.knownElements.routeIds.includes(this.ref.id);
  }
}
