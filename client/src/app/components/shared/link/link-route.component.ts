import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {Input} from '@angular/core';
import {Store} from '@ngrx/store';
import {actionRouteLink} from '../../../core/analysis/route/route.actions';
import {AppState} from '../../../core/core.state';

@Component({
  selector: 'kpn-link-route',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a [routerLink]="'/analysis/route/' + routeId" (click)="onClick()">{{title}}</a>
  `
})
export class LinkRouteComponent {

  @Input() routeId: number;
  @Input() title: string;

  constructor(private store: Store<AppState>) {
  }

  onClick(): void {
    this.store.dispatch(actionRouteLink({routeId: this.routeId.toString(), routeName: this.title}));
  }

}
