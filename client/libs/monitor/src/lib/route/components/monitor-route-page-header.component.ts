import { AsyncPipe } from '@angular/common';
import { NgClass } from '@angular/common';
import { NgFor } from '@angular/common';
import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';
import { RouterLink } from '@angular/router';
import { MonitorRouteSubRelation } from '@api/common/monitor';
import { ErrorComponent } from '@app/components/shared/error';
import { PageMenuOptionComponent } from '@app/components/shared/menu';
import { PageMenuComponent } from '@app/components/shared/menu';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { combineLatest } from 'rxjs';
import { map } from 'rxjs/operators';
import { selectMonitorRouteDescription } from '../../store/monitor.selectors';
import { selectMonitorGroupName } from '../../store/monitor.selectors';
import { selectMonitorRouteName } from '../../store/monitor.selectors';
import { actionMonitorRouteMapSelectSubRelation } from '../map/store/monitor-route-map.actions';
import { selectMonitorRouteMapNextSubRelation } from '../map/store/monitor-route-map.selectors';
import { selectMonitorRouteMapPreviousSubRelation } from '../map/store/monitor-route-map.selectors';
import { selectMonitorRouteMapSubRelations } from '../map/store/monitor-route-map.selectors';
import { MonitorRouteSubRelationMenuOptionComponent } from './monitor-route-sub-relation-menu-option.component';

@Component({
  selector: 'kpn-monitor-route-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a></li>
      <li>
        <a [routerLink]="groupLink$ | async">{{ groupName$ | async }}</a>
      </li>
      <li i18n="@@breadcrumb.monitor.route">Route</li>
    </ul>

    <h1>
      <span class="kpn-label">{{ routeName$ | async }}</span>
      <span>{{ routeDescription$ | async }}</span>
    </h1>

    <mat-menu #appMenu="matMenu" class="sub-relation-menu">
      <ng-template matMenuContent>
        <button
          mat-menu-item
          *ngFor="let subRelation of subRelations$ | async"
          (click)="select(subRelation)"
        >
          {{ subRelation.name }}
        </button>
      </ng-template>
    </mat-menu>

    <kpn-page-menu>
      <kpn-page-menu-option
        [link]="routeDetailLink$ | async"
        [active]="pageName === 'details'"
        i18n="@@monitor.route.menu.details"
      >
        Details
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="routeMapLink$ | async"
        [active]="pageName === 'map'"
        i18n="@@monitor.route.menu.map"
      >
        Map
      </kpn-page-menu-option>

      <kpn-monitor-sub-relation-menu-option
        *ngIf="pageName === 'map'"
        [routeSubRelation]="previous$ | async"
        (selectSubRelation)="select($event)"
        name="Previous"
        i18n-name="@@monitor.route.menu.previous"
      />

      <kpn-monitor-sub-relation-menu-option
        *ngIf="pageName === 'map'"
        [routeSubRelation]="next$ | async"
        (selectSubRelation)="select($event)"
        name="Next"
        i18n-name="@@monitor.route.menu.next"
      />

      <a
        *ngIf="pageName === 'map'"
        [ngClass]="{ disabled: (subrelationsEmpty$ | async) }"
        [matMenuTriggerFor]="appMenu"
        i18n="@@monitor.route.menu.select"
        >Select</a
      >
    </kpn-page-menu>

    <kpn-error />
  `,
  styles: [
    `
      ::ng-deep .sub-relation-menu {
        min-width: 30em !important;
      }

      .disabled {
        pointer-events: none;
        color: grey;
      }
    `,
  ],
  standalone: true,
  imports: [
    RouterLink,
    MatMenuModule,
    NgFor,
    PageMenuComponent,
    PageMenuOptionComponent,
    NgIf,
    MonitorRouteSubRelationMenuOptionComponent,
    NgClass,
    ErrorComponent,
    AsyncPipe,
  ],
})
export class MonitorRoutePageHeaderComponent {
  @Input() pageName: string;
  @Input() pageTitle: string;

  readonly groupName$ = this.store.select(selectMonitorGroupName);
  readonly routeName$ = this.store.select(selectMonitorRouteName);
  readonly routeDescription$ = this.store.select(selectMonitorRouteDescription);
  readonly groupLink$ = this.groupName$.pipe(
    map((groupName) => `/monitor/groups/${groupName}`)
  );

  readonly routeDetailLink$ = combineLatest([
    this.groupName$,
    this.routeName$,
  ]).pipe(
    map(
      ([groupName, routeName]) =>
        `/monitor/groups/${groupName}/routes/${routeName}`
    )
  );

  readonly routeMapLink$ = combineLatest([
    this.groupName$,
    this.routeName$,
  ]).pipe(
    map(
      ([groupName, routeName]) =>
        `/monitor/groups/${groupName}/routes/${routeName}/map`
    )
  );

  readonly subRelations$ = this.store.select(selectMonitorRouteMapSubRelations);

  readonly subrelationsEmpty$: Observable<boolean> = this.subRelations$.pipe(
    map((subRelations) => {
      return !subRelations || subRelations.length == 0;
    })
  );

  readonly previous$ = this.store.select(
    selectMonitorRouteMapPreviousSubRelation
  );

  readonly next$ = this.store.select(selectMonitorRouteMapNextSubRelation);

  constructor(private store: Store) {}

  select(subRelation: MonitorRouteSubRelation): void {
    this.store.dispatch(actionMonitorRouteMapSelectSubRelation(subRelation));
  }
}