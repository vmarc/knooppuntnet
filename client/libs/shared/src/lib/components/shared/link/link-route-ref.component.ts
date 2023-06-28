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
  selector: 'kpn-link-route-ref',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-link-route *ngIf="known" [routeId]="ref.id" [routeName]="ref.name" />
    <kpn-osm-link-relation
      *ngIf="!known"
      [relationId]="ref.id"
      [title]="ref.name"
    />
  `,
  standalone: true,
  imports: [NgIf, LinkRouteComponent, OsmLinkRelationComponent],
})
export class LinkRouteRefComponent implements OnInit {
  @Input({ required: true }) ref: Ref;
  @Input({ required: true }) knownElements: KnownElements;

  known: boolean;

  ngOnInit(): void {
    this.known = this.knownElements.routeIds.includes(this.ref.id);
  }
}
