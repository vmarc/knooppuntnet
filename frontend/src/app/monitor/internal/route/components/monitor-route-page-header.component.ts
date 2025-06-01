import { NgClass } from '@angular/common';
import { Signal } from '@angular/core';
import { output } from '@angular/core';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';
import { MonitorRouteSubRelation } from '@api/common/monitor/monitor-route-sub-relation';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageMenuOptionComponent } from '@app/shared/components/menu/page-menu-option.component';
import { PageMenuComponent } from '@app/shared/components/menu/page-menu.component';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { MonitorTranslations } from '../../components/monitor-translations';
import { MonitorRouteSubRelationMenuOptionComponent } from './monitor-route-sub-relation-menu-option.component';

@Component({
  selector: 'ui-monitor-route-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-breadcrumb [breadcrumbItems]="breadcrumbItems()" />
    <ui-page-header [pageTitle]="pageTitle()">
      {{ routeName() + ': ' + routeDescription() }}
    </ui-page-header>

    <mat-menu #appMenu="matMenu" class="sub-relation-menu">
      <ng-template matMenuContent>
        @for (subRelation of subRelations(); track $index) {
          <button mat-menu-item (click)="select(subRelation)">
            {{ subRelation.name }}
          </button>
        }
      </ng-template>
    </mat-menu>

    <ui-page-menu>
      <ui-page-menu-option
        [link]="routeDetailLink()"
        [active]="pageName() === 'details'"
        [state]="routeLinkState()"
        i18n="@@monitor.route.menu.details"
      >
        Details
      </ui-page-menu-option>

      <ui-page-menu-option
        [link]="routeMapLink()"
        [active]="pageName() === 'map'"
        [state]="routeLinkState()"
        i18n="@@monitor.route.menu.map"
      >
        Map
      </ui-page-menu-option>

      @if (pageName() === 'map') {
        <ui-monitor-sub-relation-menu-option
          [routeSubRelation]="previous()"
          (selectSubRelation)="select($event)"
          name="Previous"
          i18n-name="@@monitor.route.menu.previous"
        />

        <ui-monitor-sub-relation-menu-option
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
    </ui-page-menu>

    <ui-error />
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
  imports: [
    BreadcrumbComponent,
    ErrorComponent,
    MatMenuModule,
    MonitorRouteSubRelationMenuOptionComponent,
    NgClass,
    PageHeaderComponent,
    PageMenuComponent,
    PageMenuOptionComponent,
  ],
})
export class MonitorRoutePageHeaderComponent {
  readonly pageName = input.required<string>();
  readonly groupName = input.required<string>();
  readonly routeName = input.required<string>();
  readonly routeDescription = input.required<string>();
  readonly subRelations = input<MonitorRouteSubRelation[]>([]);
  readonly previous = input<MonitorRouteSubRelation>();
  readonly next = input<MonitorRouteSubRelation>();
  readonly selectSubRelation = output<MonitorRouteSubRelation>();
  readonly goHereInJosm = output<void>();

  protected pageTitle = computed(() => {
    const monitor = MonitorTranslations.get('monitor');
    return `${this.routeName()} | ${this.groupName()} | ${monitor}`;
  });
  protected readonly breadcrumbItems: Signal<BreadcrumbItem[]> = computed(() => {
    return [
      Breadcrumbs.home,
      Breadcrumbs.monitor,
      { routerLink: this.groupLink(), label: this.groupName() },
      { label: Breadcrumbs.monitorRouteLabel },
    ];
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
