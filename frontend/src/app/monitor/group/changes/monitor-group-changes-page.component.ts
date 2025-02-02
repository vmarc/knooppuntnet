import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { RouterLink } from '@angular/router';
import { NavService } from '@app/components/shared';
import { PageHeaderComponent } from '@app/components/shared/page';
import { PaginatorComponent } from '@app/components/shared/paginator';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';
import { PageComponent } from '../../../shared/components/shared/page/page.component';
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
      <nz-breadcrumb>
        <nz-breadcrumb-item>
          <a routerLink="/" i18n="@@breadcrumb.home">Home</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <span>Group changes</span>
        </nz-breadcrumb-item>
      </nz-breadcrumb>

      @if (service.changesState(); as state) {
        <kpn-page-header>
          {{ state.groupDescription }}
        </kpn-page-header>

        <kpn-monitor-group-page-menu pageName="changes" [groupName]="state.groupName" />

        @if (state.response; as response) {
          @if (!response.result) {
            <p>No group changes</p>
          }

          @if (response.result; as page) {
            <div class="kpn-spacer-above">
              <mat-slide-toggle
                [checked]="service.impact()"
                (change)="service.updateImpact($event.checked)"
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
    </kpn-page>
  `,
  providers: [NavService, MonitorGroupChangesPageService],
  imports: [
    MatSlideToggleModule,
    MonitorChangesComponent,
    MonitorGroupPageMenuComponent,
    NzBreadCrumbComponent,
    NzBreadCrumbItemComponent,
    PageComponent,
    PageHeaderComponent,
    PaginatorComponent,
    RouterLink,
  ],
})
export class MonitorGroupChangesPageComponent {
  readonly service = inject(MonitorGroupChangesPageService);

  pageChanged(pageIndex: number) {
    window.scroll(0, 0);
    this.service.updatePage(pageIndex);
  }
}
