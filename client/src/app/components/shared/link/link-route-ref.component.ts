import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit } from '@angular/core';
import { KnownElements } from '@api/common/common/known-elements';
import { Ref } from '@api/common/common/ref';

@Component({
  selector: 'kpn-link-route-ref',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-link-route
      *ngIf="known"
      [routeId]="ref.id"
      [title]="ref.name"
    ></kpn-link-route>
    <kpn-osm-link-relation
      *ngIf="!known"
      [relationId]="ref.id"
      [title]="ref.name"
    ></kpn-osm-link-relation>
  `,
})
export class LinkRouteRefComponent implements OnInit {
  @Input() ref: Ref;
  @Input() knownElements: KnownElements;

  known: boolean;

  ngOnInit(): void {
    this.known = this.knownElements.routeIds.contains(this.ref.id);
  }
}
