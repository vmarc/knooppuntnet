import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { Translations } from '@app/shared/i18n/translations';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { NavService } from '@app/shared/components/nav.service';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';
import { MonitorTranslations } from '../../components/monitor-translations';
import { MonitorRouteDeletePageService } from './monitor-route-delete-page.service';

@Component({
  selector: 'ui-monitor-route-delete-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.state(); as state) {
      <ui-page>
        <nz-breadcrumb>
          <nz-breadcrumb-item>
            <a routerLink="/" i18n="@@breadcrumb.home">Home</a>
          </nz-breadcrumb-item>
          <nz-breadcrumb-item>
            <a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a>
          </nz-breadcrumb-item>
          <nz-breadcrumb-item>
            <a [routerLink]="state.groupLink">{{ state.groupName }}</a>
          </nz-breadcrumb-item>
          <nz-breadcrumb-item>
            <span i18n="@@breadcrumb.monitor.route">Route</span>
          </nz-breadcrumb-item>
        </nz-breadcrumb>

        <ui-page-header [pageTitle]="pageTitle()">
          <span class="kpn-label">{{ state.routeName }}</span>
          <span>{{ state.routeDescription }}</span>
        </ui-page-header>

        <h2>{{ subtitle }}</h2>

        <ui-error />

        <div class="kpn-form">
          <p i18n="@@monitor.route.delete.comment">Remove this route from the monitor.</p>

          <p class="kpn-line">
            <mat-icon svgIcon="warning" />
            <span i18n="@@monitor.route.delete.warning">Attention: all history will be lost!</span>
          </p>

          <div class="kpn-form-buttons">
            <button mat-stroked-button (click)="service.delete()">
              <span class="kpn-warning" i18n="@@monitor.route.delete.action">Delete Route</span>
            </button>
            <a [routerLink]="state.groupLink">{{ cancelLinkText }}</a>
          </div>
        </div>
      </ui-page>
    }
  `,
  providers: [MonitorRouteDeletePageService, NavService],
  imports: [
    ErrorComponent,
    MatButtonModule,
    MatIconModule,
    NzBreadCrumbComponent,
    NzBreadCrumbItemComponent,
    PageComponent,
    PageHeaderComponent,
    RouterLink,
  ],
})
export class MonitorRouteDeletePageComponent {
  readonly subtitle = $localize`:@@monitor.route.delete.title:Delete`;
  readonly service = inject(MonitorRouteDeletePageService);
  readonly cancelLinkText = Translations.get('action.cancel');
  readonly pageTitle = computed(() => {
    const state = this.service.state();
    const monitor = MonitorTranslations.get('monitor');
    return `${this.subtitle} | ${state.routeName} | ${state.groupName} | ${monitor}`;
  });
}
