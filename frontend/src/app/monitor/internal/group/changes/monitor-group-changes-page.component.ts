import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { NavService } from '@app/shared/components/nav.service';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { OldPaginatorComponent } from '@app/shared/components/paginator/old-paginator.component';
import { MonitorChangesComponent } from '../../components/monitor-changes.component';
import { MonitorGroupPageMenuComponent } from '../components/monitor-group-page-menu.component';
import { MonitorGroupChangesPageService } from './monitor-group-changes-page.service';

@Component({
  selector: 'ui-monitor-group-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- work-in-progress -->
    <!-- eslint-disable @angular-eslint/template/i18n -->

    <ui-page>
      <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />

      @if (service.changesState(); as state) {
        <ui-page-header>
          {{ state.groupDescription }}
        </ui-page-header>

        <ui-monitor-group-page-menu pageName="changes" [groupName]="state.groupName" />

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

              <ui-old-paginator
                (pageIndexChange)="pageChanged($event)"
                [pageIndex]="page.pageIndex"
                [pageSize]="page.pageSize"
                [length]="page.totalChangeCount"
                [showPageSizeSelection]="true"
              />

              <ui-monitor-changes
                [pageSize]="page.pageSize"
                [pageIndex]="page.pageIndex"
                [changes]="page.changes"
              />
            </div>
          }
        }
      }
    </ui-page>
  `,
  providers: [NavService, MonitorGroupChangesPageService],
  imports: [
    BreadcrumbComponent,
    MatSlideToggleModule,
    MonitorChangesComponent,
    MonitorGroupPageMenuComponent,
    OldPaginatorComponent,
    PageComponent,
    PageHeaderComponent,
  ],
})
export class MonitorGroupChangesPageComponent {
  protected readonly service = inject(MonitorGroupChangesPageService);
  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    Breadcrumbs.home,
    Breadcrumbs.monitor,
    { label: Breadcrumbs.groupChangesLabel },
  ];

  pageChanged(pageIndex: number) {
    window.scroll(0, 0);
    this.service.updatePage(pageIndex);
  }
}
