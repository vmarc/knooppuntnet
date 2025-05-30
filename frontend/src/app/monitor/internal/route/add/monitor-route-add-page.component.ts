import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { NavService } from '@app/shared/components/nav.service';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';
import { MonitorTranslations } from '../../components/monitor-translations';
import { MonitorRouteFormComponent } from '../components/monitor-route-form.component';
import { MonitorRouteAddPageService } from './monitor-route-add-page.service';

@Component({
  selector: 'ui-monitor-route-add-page',
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
          {{ state.groupDescription }}
        </ui-page-header>

        <h2>{{ subtitle }}</h2>

        <ui-error />

        <ui-monitor-route-form
          mode="add"
          [groupName]="state.groupName"
          [initialProperties]="{ groupName: state.groupName }"
        />
      </ui-page>
    }
  `,
  providers: [MonitorRouteAddPageService, NavService],
  imports: [
    ErrorComponent,
    MonitorRouteFormComponent,
    NzBreadCrumbComponent,
    NzBreadCrumbItemComponent,
    PageComponent,
    PageHeaderComponent,
    RouterLink,
  ],
})
export class MonitorRouteAddPageComponent {
  readonly subtitle = $localize`:@@monitor.route.add.title:Add route`;
  readonly service = inject(MonitorRouteAddPageService);
  readonly pageTitle = computed(() => {
    const groupName = this.service.state().groupName;
    const monitor = MonitorTranslations.get('monitor');
    return `${this.subtitle} | ${groupName} | ${monitor}`;
  });
}
