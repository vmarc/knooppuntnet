import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { PageMenuOptionComponent } from '@app/shared/components/menu/page-menu-option.component';
import { PageMenuComponent } from '@app/shared/components/menu/page-menu.component';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { RouteService } from '../route.service';

@Component({
  selector: 'ui-route-page-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page-header [pageTitle]="service.routeDisplayName()" subject="route-page">
      <span>{{ service.routeDisplayName() }}</span>
    </ui-page-header>

    <ui-page-menu>
      <ui-page-menu-option
        [link]="routeLink()"
        [active]="pageName() === 'details'"
        i18n="@@route.menu.details"
      >
        Details
      </ui-page-menu-option>

      <ui-page-menu-option
        [link]="mapLink()"
        [active]="pageName() === 'map'"
        i18n="@@route.menu.map"
      >
        Map
      </ui-page-menu-option>

      <ui-page-menu-option
        [link]="changesLink()"
        [active]="pageName() === 'changes'"
        [elementCount]="service.changeCount()"
        i18n="@@route.menu.changes"
      >
        Changes
      </ui-page-menu-option>

      <ui-page-menu-option
        [link]="segmentsLink()"
        [active]="pageName() === 'segments'"
        [elementCount]="3"
        i18n="@@route.menu.segments"
        >Segments
      </ui-page-menu-option>
    </ui-page-menu>
  `,
  imports: [MatIconModule, PageHeaderComponent, PageMenuComponent, PageMenuOptionComponent],
})
export class RoutePageHeaderComponent {
  readonly pageName = input.required<string>();
  protected readonly service = inject(RouteService);
  protected readonly routeLink = computed(() => `/analysis/route/${this.service.routeId()}`);
  protected readonly mapLink = computed(() => this.routeLink() + '/map');
  protected readonly changesLink = computed(() => this.routeLink() + '/changes');
  protected readonly segmentsLink = computed(() => this.routeLink() + '/segments');
}
