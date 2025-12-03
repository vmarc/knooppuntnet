import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Translations } from '@app/shared/i18n/translations';
import { NavService } from '@app/shared/components/nav.service';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzFormDirective } from 'ng-zorro-antd/form';
import { MonitorGroupBreadcrumbComponent } from '../components/monitor-group-breadcrumb.component';
import { MonitorGroupDescriptionComponent } from '../components/monitor-group-description.component';
import { MonitorGroupNameComponent } from '../components/monitor-group-name.component';
import { MonitorGroupUpdatePageService } from './monitor-group-update-page.service';

@Component({
  selector: 'ui-monitor-group-update-page',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <ui-page>
      <ui-monitor-group-breadcrumb />

      <ui-page-header>
        <ng-container i18n="@@monitor.group.update.title">Monitor - update group</ng-container>
      </ui-page-header>

      @if (state(); as state) {
        @if (state.response; as response) {
          @if (!response.result) {
            <div>
              <p i18n="@@monitor.group.update.group-not-found">Group not found</p>
            </div>
          }
          <!-- eslint-disable-next-line @angular-eslint/template/prefer-at-else -->
          @if (response.result; as page) {
            <div>
              <form nz-form nzLayout="vertical" [formGroup]="form" #ngForm="ngForm">
                <ui-monitor-group-name [ngForm]="ngForm" [name]="name" />
                <ui-monitor-group-description [ngForm]="ngForm" [description]="description" />

                <div class="kpn-form-buttons">
                  <button
                    nz-button
                    nzType="primary"
                    (click)="update(page.groupId)"
                    i18n="@@monitor.group.update.action"
                  >
                    Update group
                  </button>
                  <a routerLink="/monitor">{{ cancelLinkText }}</a>
                </div>
              </form>
            </div>
          }
        }
      }
    </ui-page>
  `,
  providers: [MonitorGroupUpdatePageService, NavService],
  imports: [
    MonitorGroupBreadcrumbComponent,
    MonitorGroupDescriptionComponent,
    MonitorGroupNameComponent,
    NzButtonComponent,
    NzFormDirective,
    PageComponent,
    PageHeaderComponent,
    ReactiveFormsModule,
    RouterLink,
  ],
})
export class MonitorGroupUpdatePageComponent {
  private readonly service = inject(MonitorGroupUpdatePageService);
  protected readonly state = this.service.state;
  protected readonly form = this.service.form;
  protected readonly name = this.service.name;
  protected readonly description = this.service.description;

  readonly cancelLinkText = Translations.get('action.cancel');

  update(groupId: string): void {
    this.service.update(groupId);
  }
}
