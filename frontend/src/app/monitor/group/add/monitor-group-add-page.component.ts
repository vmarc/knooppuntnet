import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { NavService } from '@app/components/shared';
import { FormStatusComponent } from '@app/components/shared';
import { PageComponent } from '@app/components/shared/page';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { MonitorGroupBreadcrumbComponent } from '../components/monitor-group-breadcrumb.component';
import { MonitorGroupDescriptionComponent } from '../components/monitor-group-description.component';
import { MonitorGroupNameComponent } from '../components/monitor-group-name.component';
import { MonitorGroupAddPageService } from './monitor-group-add-page.service';

@Component({
  selector: 'kpn-monitor-group-add-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-monitor-group-breadcrumb />

      <h1 i18n="@@monitor.group.add.title">Monitor - add group</h1>

      <div class="kpn-comment">
        <p i18n="@@monitor.group.add.comment.1">
          Create a new group containing routes to be monitored.
        </p>
        <p i18n="@@monitor.group.add.comment.2">
          Provide a short name (that will be used in the browser address), and a title for the group
          (probably describing who will be maintaining the route group).
        </p>
      </div>

      <form [formGroup]="service.form" class="kpn-form" #ngForm="ngForm">
        <kpn-monitor-group-name [ngForm]="ngForm" [name]="service.name" />
        <kpn-monitor-group-description [ngForm]="ngForm" [description]="service.description" />
        <kpn-form-status
          formName="group-form"
          [statusChanges]="service.form.statusChanges"
        ></kpn-form-status>
        <div class="kpn-form-buttons">
          <button
            mat-stroked-button
            id="add-group"
            (click)="add()"
            i18n="@@monitor.group.add.action"
          >
            Add group
          </button>
          <a id="cancel" routerLink="/monitor" i18n="@@action.cancel">Cancel</a>
        </div>
      </form>
      <kpn-sidebar sidebar />
    </kpn-page>
  `,
  providers: [MonitorGroupAddPageService, NavService],
  standalone: true,
  imports: [
    FormStatusComponent,
    MatButtonModule,
    MonitorGroupBreadcrumbComponent,
    MonitorGroupDescriptionComponent,
    MonitorGroupDescriptionComponent,
    MonitorGroupNameComponent,
    PageComponent,
    ReactiveFormsModule,
    RouterLink,
    SidebarComponent,
  ],
})
export class MonitorGroupAddPageComponent {
  protected readonly service = inject(MonitorGroupAddPageService);

  add(): void {
    this.service.add();
  }
}
