import { AsyncPipe } from '@angular/common';
import { NgClass } from '@angular/common';
import { computed } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';
import { RouterLink } from '@angular/router';
import { MonitorRouteSubRelation } from '@api/common/monitor';
import { ErrorComponent } from '@app/components/shared/error';
import { EllipsisComponent } from '@app/components/shared/format';
import { PageMenuOptionComponent } from '@app/components/shared/menu';
import { PageMenuComponent } from '@app/components/shared/menu';
import { PageHeaderComponent } from '@app/components/shared/page';
import { MonitorTranslations } from '../../components/monitor-translations';
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

    <kpn-page-header [pageTitle]="pageTitle()">
      {{ routeName() + ': ' + routeDescription() }}
    </kpn-page-header>

    <mat-menu #appMenu="matMenu" class="sub-relation-menu">
      <ng-template matMenuContent>
        @for (subRelation of subRelations(); track $index) {
          <button mat-menu-item (click)="select(subRelation)">
            {{ subRelation.name }}
          </button>
        }
      </ng-template>
    </mat-menu>

    <kpn-page-menu>
      <kpn-page-menu-option
        [link]="routeDetailLink()"
        [active]="pageName() === 'details'"
        [state]="routeLinkState()"
        i18n="@@monitor.route.menu.details"
      >
        Details
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="routeMapLink()"
        [active]="pageName() === 'map'"
        [state]="routeLinkState()"
        i18n="@@monitor.route.menu.map"
      >
        Map
      </kpn-page-menu-option>

      @if (pageName() === 'map') {
        <kpn-monitor-sub-relation-menu-option
          [routeSubRelation]="previous()"
          (selectSubRelation)="select($event)"
          name="Previous"
          i18n-name="@@monitor.route.menu.previous"
        />

        <kpn-monitor-sub-relation-menu-option
          [routeSubRelation]="next()"
          (selectSubRelation)="select($event)"
          name="Next"
          i18n-name="@@monitor.route.menu.next"
        />

        <a
          [ngClass]="{ disabled: subrelationsEmpty() }"
          [matMenuTriggerFor]="appMenu"
          i18n="@@monitor.route.menu.select"
        >
          Select
        </a>
      }

      <span menu-extra-item>
        @if (pageName() === 'map') {
          <a
            rel="nofollow"
            (click)="josm()"
            title="Go here in JOSM"
            i18n-title="@@monitor.route.menu.josm.title"
            i18n="@@monitor.route.menu.josm"
          >
            josm
          </a>
        }
      </span>
    </kpn-page-menu>

    <kpn-error />
  `,
  styles: `
    ::ng-deep .sub-relation-menu {
      min-width: 30em !important;
    }

    .disabled {
      pointer-events: none;
      color: grey;
    }
  `,
  standalone: true,
  imports: [
    AsyncPipe,
    EllipsisComponent,
    ErrorComponent,
    MatMenuModule,
    MonitorRouteSubRelationMenuOptionComponent,
    NgClass,
    PageMenuComponent,
    PageMenuOptionComponent,
    RouterLink,
    PageHeaderComponent,
  ],
})
export class MonitorRoutePageHeaderComponent {
  pageName = input.required<string>();
  groupName = input.required<string>();
  routeName = input.required<string>();
  routeDescription = input.required<string>();
  subRelations = input<MonitorRouteSubRelation[]>([]);
  previous = input<MonitorRouteSubRelation>();
  next = input<MonitorRouteSubRelation>();
  @Output() selectSubRelation = new EventEmitter<MonitorRouteSubRelation>();
  @Output() goHereInJosm = new EventEmitter<void>();

  protected pageTitle = computed(() => {
    const monitor = MonitorTranslations.get('monitor');
    return `${this.routeName()} | ${this.groupName()} | ${monitor}`;
  });

  select(subRelation: MonitorRouteSubRelation): void {
    this.selectSubRelation.emit(subRelation);
  }

  groupLink(): string {
    return `/monitor/groups/${this.groupName()}`;
  }

  routeDetailLink(): string {
    return `/monitor/groups/${this.groupName()}/routes/${this.routeName()}`;
  }

  routeMapLink(): string {
    return `/monitor/groups/${this.groupName()}/routes/${this.routeName()}/map`;
  }

  routeLinkState() {
    return { description: this.routeDescription() };
  }

  subrelationsEmpty(): boolean {
    return !this.subRelations() || this.subRelations().length === 0;
  }

  josm(): void {
    this.goHereInJosm.emit();
  }
}
