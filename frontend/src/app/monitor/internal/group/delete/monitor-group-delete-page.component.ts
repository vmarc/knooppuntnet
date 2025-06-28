import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MonitorGroupDeleteInfoComponent } from '@app/monitor/internal/group/delete/monitor-group-delete-info.component';
import { Translations } from '@app/shared/i18n/translations';
import { NavService } from '@app/shared/components/nav.service';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { MonitorGroupBreadcrumbComponent } from '../components/monitor-group-breadcrumb.component';
import { MonitorGroupDeletePageService } from './monitor-group-delete-page.service';

@Component({
  selector: 'ui-monitor-group-delete-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-monitor-group-breadcrumb />

      <ui-page-header>
        <ng-container i18n="@@monitor.group.delete.title">Monitor - delete group</ng-container>
      </ui-page-header>

      @if (service.state(); as state) {
        @if (state.response; as response) {
          @if (!response.result) {
            <p class="kpn-error" i18n="@@monitor.group.delete.group-not-found">Group not found</p>
          }
          @if (response.result; as page) {
            <ui-monitor-group-delete-info [page]="page" />
            <div class="kpn-form-buttons">
              <button nz-button nzType="primary" (click)="service.delete(page.groupId)">
                <span i18n="@@monitor.group.delete.action">Delete group</span>
              </button>
              <a routerLink="/monitor">{{ cancelLinkText }}</a>
            </div>
          }
        }
      }
    </ui-page>
  `,
  providers: [MonitorGroupDeletePageService, NavService],
  imports: [
    MonitorGroupBreadcrumbComponent,
    MonitorGroupDeleteInfoComponent,
    NzButtonComponent,
    PageComponent,
    PageHeaderComponent,
    RouterLink,
  ],
})
export class MonitorGroupDeletePageComponent {
  readonly service = inject(MonitorGroupDeletePageService);
  readonly cancelLinkText = Translations.get('action.cancel');
}
