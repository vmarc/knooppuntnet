import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { NavService } from '@app/components/shared';
import { PageComponent } from '@app/components/shared/page';
import { SidebarComponent } from '@app/components/shared/sidebar';
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

      <h1 i18n="@@monitor.group.update.title">Monitor - update group</h1>

      <div *ngIf="service.apiResponse() as response" class="kpn-form">
        <div *ngIf="!response.result">
          <p i18n="@@monitor.group.update.group-not-found">Group not found</p>
        </div>
        <div *ngIf="response.result as page">
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
              <a routerLink="/monitor" i18n="@@action.cancel">Cancel</a>
            </div>
          </form>
        </div>
      </div>
      <kpn-sidebar sidebar />
    </kpn-page>
  `,
  providers: [MonitorGroupUpdatePageService, NavService],
  standalone: true,
  imports: [
    MatButtonModule,
    MonitorGroupBreadcrumbComponent,
    MonitorGroupDescriptionComponent,
    MonitorGroupNameComponent,
    NgIf,
    PageComponent,
    ReactiveFormsModule,
    RouterLink,
    SidebarComponent,
  ],
})
export class MonitorGroupUpdatePageComponent {
  constructor(protected service: MonitorGroupUpdatePageService) {}
}
