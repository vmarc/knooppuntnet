import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {Util} from '../../components/shared/util';
import {AppState} from '../../core/core.state';
import {selectLongDistanceRouteName} from '../../core/longdistance/long-distance.selectors';
import {selectLongDistanceRouteChange} from '../../core/longdistance/long-distance.selectors';

@Component({
  selector: 'kpn-long-distance-route-change-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/long-distance/routes">Long distance routes</a></li>
      <li>Route</li>
    </ul>

    <h1 class="title">
      {{routeName$ | async}}
    </h1>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result">
        Route not found
      </div>
      <div *ngIf="response.result">
        <pre>
{{util.json(response)}}
        </pre>
      </div>
    </div>
  `,
  styles: [`
  `]
})
export class LongDistanceRouteChangePageComponent {

  util = Util;

  routeName$ = this.store.select(selectLongDistanceRouteName);
  response$ = this.store.select(selectLongDistanceRouteChange);

  constructor(private store: Store<AppState>) {
  }

}
