import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { MonitorRouteSubRelation } from '@api/common/monitor/monitor-route-sub-relation';
import { Store } from '@ngrx/store';
import { toLonLat } from 'ol/proj';
import { Observable } from 'rxjs';
import { combineLatest } from 'rxjs';
import { map } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { actionMonitorRouteMapSelectSubRelation } from '../../store/monitor.actions';
import { selectMonitorRouteMapPage } from '../../store/monitor.selectors';
import { selectMonitorRouteDescription } from '../../store/monitor.selectors';
import { selectMonitorGroupName } from '../../store/monitor.selectors';
import { selectMonitorRouteName } from '../../store/monitor.selectors';
import { MonitorRouteMapService } from '../map/monitor-route-map.service';

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
        (select)="select($event)"
        name="Previous"
        i18n-name="@@monitor.route.menu.previous"
      />

      <kpn-monitor-sub-relation-menu-option
        *ngIf="pageName === 'map'"
        [routeSubRelation]="next$ | async"
        (select)="select($event)"
        name="Next"
        i18n-name="@@monitor.route.menu.next"
      />

      <kpn-page-menu-option
        *ngIf="pageName === 'map'"
        [matMenuTriggerFor]="appMenu"
        i18n="@@monitor.route.menu.select"
      >
        Select
      </kpn-page-menu-option>

      <div *ngIf="pageName === 'map'" class="menu-extra-item">
        <a (click)="gotoOpenstreetmap()" class="external">
          {{ osmLinkLabel }}
        </a>
        <a (click)="editWithId()" class="external id-link">
          {{ idEditorLinkLabel }}
        </a>
      </div>
    </kpn-page-menu>

    <kpn-error/>
  `,
  styles: [
    `
      .id-link {
        padding-left: 1em;
      }

      ::ng-deep .sub-relation-menu {
        min-width: 30em !important;
      }
    `,
  ],
})
export class MonitorRoutePageHeaderComponent {
  @Input() pageName: string;
  @Input() pageTitle: string;

  readonly osmLinkLabel = 'openstreetmap.org';
  readonly idEditorLinkLabel = 'iD';

  readonly groupName$ = this.store.select(selectMonitorGroupName);
  readonly routeName$ = this.store.select(selectMonitorRouteName);
  readonly routeDescription$ = this.store.select(selectMonitorRouteDescription);
  readonly groupLink$ = this.groupName$.pipe(
    map((groupName) => `/monitor/groups/${groupName}`)
  );

  readonly routeDetailLink$ = combineLatest([
    this.store.select(selectMonitorGroupName),
    this.store.select(selectMonitorRouteName),
  ]).pipe(
    map(
      ([groupName, routeName]) =>
        `/monitor/groups/${groupName}/routes/${routeName}`
    )
  );

  readonly routeMapLink$ = combineLatest([
    this.store.select(selectMonitorGroupName),
    this.store.select(selectMonitorRouteName),
  ]).pipe(
    map(
      ([groupName, routeName]) =>
        `/monitor/groups/${groupName}/routes/${routeName}/map`
    )
  );

  readonly subRelations$: Observable<MonitorRouteSubRelation[]> = this.store
    .select(selectMonitorRouteMapPage)
    .pipe(
      map((page) => {
        return page?.result?.subRelations;
      })
    );

  readonly previous$: Observable<MonitorRouteSubRelation> = this.store
    .select(selectMonitorRouteMapPage)
    .pipe(map((page) => page?.result?.prevSubRelation));

  readonly next$: Observable<MonitorRouteSubRelation> = this.store
    .select(selectMonitorRouteMapPage)
    .pipe(
      map((page) => {
        return page?.result?.nextSubRelation;
      })
    );

  constructor(
    private store: Store<AppState>,
    private mapService: MonitorRouteMapService
  ) {}

  gotoOpenstreetmap(): void {
    const zoom = Math.round(this.mapService.getView().getZoom());
    const center = toLonLat(this.mapService.getView().getCenter());
    const url = `https://www.openstreetmap.org/#map=${zoom}/${center[1]}/${center[0]}`;
    window.open(url, '_blank');
  }

  editWithId(): void {
    const zoom = Math.round(this.mapService.getView().getZoom());
    const center = toLonLat(this.mapService.getView().getCenter());
    const url = `https://www.openstreetmap.org/edit?editor=id#map=${zoom}/${center[1]}/${center[0]}`;
    window.open(url, '_blank');
  }

  select(subRelation: MonitorRouteSubRelation): void {
    this.store.dispatch(actionMonitorRouteMapSelectSubRelation(subRelation));
  }
}
