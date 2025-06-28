import { Signal } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { IconWarningComponent } from '@app/shared/components/icon/icon-warning.component';
import { Translations } from '@app/shared/i18n/translations';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { NavService } from '@app/shared/components/nav.service';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { MonitorTranslations } from '../../components/monitor-translations';
import { MonitorRouteDeletePageService } from './monitor-route-delete-page.service';

@Component({
  selector: 'ui-monitor-route-delete-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.state(); as state) {
      <ui-page>
        <ui-breadcrumb [breadcrumbItems]="breadcrumbItems()" />
        <ui-page-header [pageTitle]="pageTitle()">
          <span class="kpn-label">{{ state.routeName }}</span>
          <span>{{ state.routeDescription }}</span>
        </ui-page-header>

        <h2>{{ subtitle }}</h2>

        <ui-error />

        <div class="kpn-form">
          <p i18n="@@monitor.route.delete.comment">Remove this route from the monitor.</p>

          <p class="kpn-line">
            <ui-icon-warning />
            <span i18n="@@monitor.route.delete.warning">Attention: all history will be lost!</span>
          </p>

          <div class="kpn-form-buttons">
            <button nz-button nzType="primary" (click)="service.delete()">
              <span i18n="@@monitor.route.delete.action">Delete Route</span>
            </button>
            <a [routerLink]="state.groupLink">{{ cancelLinkText }}</a>
          </div>
        </div>
      </ui-page>
    }
  `,
  providers: [MonitorRouteDeletePageService, NavService],
  imports: [
    BreadcrumbComponent,
    ErrorComponent,
    IconWarningComponent,
    NzButtonComponent,
    PageComponent,
    PageHeaderComponent,
    RouterLink,
  ],
})
export class MonitorRouteDeletePageComponent {
  protected readonly subtitle = $localize`:@@monitor.route.delete.title:Delete`;
  protected readonly service = inject(MonitorRouteDeletePageService);
  protected readonly cancelLinkText = Translations.get('action.cancel');
  protected readonly pageTitle = computed(() => {
    const state = this.service.state();
    const monitor = MonitorTranslations.get('monitor');
    return `${this.subtitle} | ${state.routeName} | ${state.groupName} | ${monitor}`;
  });
  private readonly groupLink = computed(() => this.service.state().groupLink);
  private readonly groupName = computed(() => this.service.state().groupName);
  protected readonly breadcrumbItems: Signal<BreadcrumbItem[]> = computed(() => {
    return [
      Breadcrumbs.home,
      Breadcrumbs.monitor,
      { routerLink: this.groupLink(), label: this.groupName() },
      { label: Breadcrumbs.monitorRouteLabel },
    ];
  });
}
