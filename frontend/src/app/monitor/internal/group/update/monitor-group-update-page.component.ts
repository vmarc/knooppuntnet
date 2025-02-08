import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { Translations } from '@app/i18n';
import { NavService } from '@app/shared/components/nav.service';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { MonitorGroupBreadcrumbComponent } from '../components/monitor-group-breadcrumb.component';
import { MonitorGroupDescriptionComponent } from '../components/monitor-group-description.component';
import { MonitorGroupNameComponent } from '../components/monitor-group-name.component';
import { MonitorGroupUpdatePageService } from './monitor-group-update-page.service';

@Component({
  selector: 'kpn-monitor-group-update-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-monitor-group-breadcrumb />

      <kpn-page-header>
        <ng-container i18n="@@monitor.group.update.title">Monitor - update group</ng-container>
      </kpn-page-header>

      @if (service.state(); as state) {
        @if (state.response; as response) {
          <div class="kpn-form">
            @if (!response.result) {
              <div>
                <p i18n="@@monitor.group.update.group-not-found">Group not found</p>
              </div>
            }

            @if (response.result; as page) {
              <div>
                <form [formGroup]="service.form" #ngForm="ngForm">
                  <kpn-monitor-group-name [ngForm]="ngForm" [name]="service.name" />
                  <kpn-monitor-group-description
                    [ngForm]="ngForm"
                    [description]="service.description"
                  />

                  <div class="kpn-form-buttons">
                    <button
                      mat-stroked-button
                      (click)="service.update(page.groupId)"
                      i18n="@@monitor.group.update.action"
                    >
                      Update group
                    </button>
                    <a routerLink="/monitor">{{ cancelLinkText }}</a>
                  </div>
                </form>
              </div>
            }
          </div>
        }
      }
    </kpn-page>
  `,
  providers: [MonitorGroupUpdatePageService, NavService],
  imports: [
    MatButtonModule,
    MonitorGroupBreadcrumbComponent,
    MonitorGroupDescriptionComponent,
    MonitorGroupNameComponent,
    PageComponent,
    PageHeaderComponent,
    ReactiveFormsModule,
    RouterLink,
  ],
})
export class MonitorGroupUpdatePageComponent {
  readonly service = inject(MonitorGroupUpdatePageService);
  readonly cancelLinkText = Translations.get('action.cancel');
}
