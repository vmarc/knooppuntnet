import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit } from '@angular/core';
import { KnownElements } from '@api/common/common';
import { Ref } from '@api/common/common';

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
})
export class LinkRouteRefComponent implements OnInit {
  @Input() ref: Ref;
  @Input() knownElements: KnownElements;

  known: boolean;

  ngOnInit(): void {
    this.known = this.knownElements.routeIds.includes(this.ref.id);
  }
}
