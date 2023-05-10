import { AsyncPipe } from '@angular/common';
import { NgClass } from '@angular/common';
import { NgFor } from '@angular/common';
import { NgIf } from '@angular/common';
import { computed } from '@angular/core';
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
        <a [routerLink]="groupLink()">{{ groupName() }}</a>
      </li>
      <li i18n="@@breadcrumb.monitor.route">Route</li>
    </ul>

    <h1>
      <span class="kpn-label">{{ routeName() }}</span>
      <span>{{ routeDescription() }}</span>
    </h1>

    <mat-menu #appMenu="matMenu" class="sub-relation-menu">
      <ng-template matMenuContent>
        <button
          mat-menu-item
          *ngFor="let subRelation of subRelations()"
          (click)="select(subRelation)"
        >
          {{ subRelation.name }}
        </button>
      </ng-template>
    </mat-menu>

    <kpn-page-menu>
      <kpn-page-menu-option
        [link]="routeDetailLink()"
        [active]="pageName === 'details'"
        i18n="@@monitor.route.menu.details"
      >
        Details
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="routeMapLink()"
        [active]="pageName === 'map'"
        i18n="@@monitor.route.menu.map"
      >
        Map
      </kpn-page-menu-option>

      <kpn-monitor-sub-relation-menu-option
        *ngIf="pageName === 'map'"
        [routeSubRelation]="previous()"
        (selectSubRelation)="select($event)"
        name="Previous"
        i18n-name="@@monitor.route.menu.previous"
      />

      <kpn-monitor-sub-relation-menu-option
        *ngIf="pageName === 'map'"
        [routeSubRelation]="next()"
        (selectSubRelation)="select($event)"
        name="Next"
        i18n-name="@@monitor.route.menu.next"
      />

      <a
        *ngIf="pageName === 'map'"
        [ngClass]="{ disabled: subrelationsEmpty() }"
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

  readonly groupName = this.store.selectSignal(selectMonitorGroupName);
  readonly routeName = this.store.selectSignal(selectMonitorRouteName);
  readonly routeDescription = this.store.selectSignal(
    selectMonitorRouteDescription
  );
  readonly groupLink = computed(() => `/monitor/groups/${this.groupName()}`);
  readonly routeDetailLink = computed(
    () => `/monitor/groups/${this.groupName()}/routes/${this.routeName()}`
  );
  readonly routeMapLink = computed(
    () => `/monitor/groups/${this.groupName()}/routes/${this.routeName()}/map`
  );

  readonly subRelations = this.store.selectSignal(
    selectMonitorRouteMapSubRelations
  );

  readonly subrelationsEmpty = computed(() => {
    const sr = this.subRelations();
    return !sr || sr.length == 0;
  });

  readonly previous = this.store.selectSignal(
    selectMonitorRouteMapPreviousSubRelation
  );

  readonly next = this.store.selectSignal(selectMonitorRouteMapNextSubRelation);

  constructor(private store: Store) {}

  select(subRelation: MonitorRouteSubRelation): void {
    this.store.dispatch(actionMonitorRouteMapSelectSubRelation(subRelation));
  }
}
