import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { RouterLink } from '@angular/router';
import { NavService } from '@app/components/shared';
import { PageComponent } from '@app/components/shared/page';
import { PaginatorComponent } from '@app/components/shared/paginator';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { MonitorChangesComponent } from '../../components/monitor-changes.component';
import { MonitorGroupPageMenuComponent } from '../components/monitor-group-page-menu.component';
import { MonitorGroupChangesPageService } from './monitor-group-changes-page.service';

@Component({
  selector: 'kpn-monitor-group-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- work-in-progress -->
    <!-- eslint-disable @angular-eslint/template/i18n -->

    <kpn-page>
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a>
        </li>
        <li>Group changes</li>
      </ul>

      @if (service.state(); as state) {
        <h1>
          {{ state.groupDescription }}
        </h1>

        <kpn-monitor-group-page-menu pageName="changes" [groupName]="state.groupName" />

        @if (state.response; as response) {
          @if (!response.result) {
            <p>No group changes</p>
          }

          @if (response.result; as page) {
            <div class="kpn-spacer-above">
              <mat-slide-toggle
                [checked]="service.impact()"
                (change)="service.impactChanged($event.checked)"
              >
                Impact
              </mat-slide-toggle>

              <kpn-paginator
                (pageIndexChange)="pageChanged($event)"
                [pageIndex]="page.pageIndex"
                [pageSize]="page.pageSize"
                [length]="page.totalChangeCount"
                [showPageSizeSelection]="true"
              />

              <kpn-monitor-changes
                [pageSize]="page.pageSize"
                [pageIndex]="page.pageIndex"
                [changes]="page.changes"
              />
            </div>
          }
        }
      }
      <kpn-sidebar sidebar />
    </kpn-page>
  `,
  providers: [NavService, MonitorGroupChangesPageService],
  standalone: true,
  imports: [
    MatSlideToggleModule,
    MonitorChangesComponent,
    MonitorGroupPageMenuComponent,
    PageComponent,
    PaginatorComponent,
    RouterLink,
    SidebarComponent,
  ],
})
export class MonitorGroupChangesPageComponent {
  protected readonly service = inject(MonitorGroupChangesPageService);

  pageChanged(pageIndex: number) {
    window.scroll(0, 0);
    this.service.pageChanged(pageIndex);
  }
}
