import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { NavService } from '@app/components/shared';
import { ErrorComponent } from '@app/components/shared/error';
import { PageHeaderComponent } from '@app/components/shared/page';
import { PageComponent } from '@app/components/shared/page';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { MonitorTranslations } from '../../components/monitor-translations';
import { MonitorRouteDeletePageService } from './monitor-route-delete-page.service';

@Component({
  selector: 'kpn-monitor-route-delete-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.state(); as state) {
      <kpn-page>
        <ul class="breadcrumb">
          <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
          <li>
            <a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a>
          </li>
          <li>
            <a [routerLink]="state.groupLink">{{ state.groupName }}</a>
          </li>
          <li i18n="@@breadcrumb.monitor.route">Route</li>
        </ul>

        <kpn-page-header [pageTitle]="pageTitle()">
          <span class="kpn-label">{{ state.routeName }}</span>
          <span>{{ state.routeDescription }}</span>
        </kpn-page-header>

        <h2>{{ subtitle }}</h2>

        <kpn-error />

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
            <a [routerLink]="state.groupLink" i18n="@@action.cancel">Cancel</a>
          </div>
        </div>
        <kpn-sidebar sidebar />
      </kpn-page>
    }
  `,
  providers: [MonitorRouteDeletePageService, NavService],
  standalone: true,
  imports: [
    ErrorComponent,
    MatButtonModule,
    MatIconModule,
    PageComponent,
    RouterLink,
    SidebarComponent,
    PageHeaderComponent,
  ],
})
export class MonitorRouteDeletePageComponent {
  protected readonly subtitle = $localize`:@@monitor.route.delete.title:Delete`;
  protected readonly service = inject(MonitorRouteDeletePageService);
  protected readonly pageTitle = computed(() => {
    const state = this.service.state();
    const monitor = MonitorTranslations.translate('monitor');
    return `${this.subtitle} | ${state.routeName} | ${state.groupName} | ${monitor}`;
  });
}
