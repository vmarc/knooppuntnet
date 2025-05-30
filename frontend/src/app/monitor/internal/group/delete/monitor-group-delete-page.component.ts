import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { Translations } from '@app/shared/i18n/translations';
import { NavService } from '@app/shared/components/nav.service';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
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
            <div class="kpn-form">
              <p>
                <span class="kpn-label" i18n="@@monitor.group.delete.name"> Name </span>
                {{ page.groupName }}
              </p>

              <p>
                <span class="kpn-label" i18n="@@monitor.group.delete.description">
                  Description
                </span>
                {{ page.groupDescription }}
              </p>

              @if (page.routes.length; as routeCount) {
                @if (routeCount > 0) {
                  <div class="kpn-line">
                    <mat-icon svgIcon="warning" />
                    <span i18n="@@monitor.group.delete.warning">
                      The information of all routes ({{ routeCount }} route(s)) in the group will
                      also be deleted!
                    </span>
                  </div>
                }
              }

              <div class="kpn-form-buttons">
                <button mat-stroked-button (click)="service.delete(page.groupId)">
                  <span class="kpn-warning" i18n="@@monitor.group.delete.action">
                    Delete group
                  </span>
                </button>
                <a routerLink="/monitor">{{ cancelLinkText }}</a>
              </div>
            </div>
          }
        }
      }
    </ui-page>
  `,
  providers: [MonitorGroupDeletePageService, NavService],
  imports: [
    MatButtonModule,
    MatIconModule,
    MonitorGroupBreadcrumbComponent,
    PageComponent,
    PageHeaderComponent,
    RouterLink,
  ],
})
export class MonitorGroupDeletePageComponent {
  readonly service = inject(MonitorGroupDeletePageService);
  readonly cancelLinkText = Translations.get('action.cancel');
}
