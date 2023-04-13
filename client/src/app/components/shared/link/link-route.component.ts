import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { NetworkType } from '@api/custom';
import { actionRouteLink } from '@app/analysis/route/store/route.actions';
import { Store } from '@ngrx/store';

@Component({
  selector: 'kpn-link-route',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a [routerLink]="'/analysis/route/' + routeId" (click)="onClick()">{{
      title
    }}</a>
  `,
})
export class LinkRouteComponent {
  @Input() routeId: number;
  @Input() title: string;
  @Input() networkType: NetworkType;

  constructor(private store: Store) {}

  onClick(): void {
    this.store.dispatch(
      actionRouteLink({
        routeId: this.routeId.toString(),
        routeName: this.title,
        networkType: this.networkType,
      })
    );
  }
}
