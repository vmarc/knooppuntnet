import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { NavService } from '@app/components/shared';
import { PageComponent } from '@app/components/shared/page';
import { PaginatorComponent } from '@app/components/shared/paginator';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { MonitorChangesComponent } from '../../components/monitor-changes.component';
import { MonitorRoutePageHeaderComponent } from '../components/monitor-route-page-header.component';
import { MonitorRouteChangesPageService } from './monitor-route-changes-page.service';

@Component({
  selector: 'kpn-monitor-route-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- work-in-progress -->
    <!-- eslint-disable @angular-eslint/template/i18n -->

    @if (service.state(); as state) {
      <kpn-page>
        <kpn-monitor-route-page-header
          pageName="changes"
          [groupName]="state.groupName"
          [routeName]="state.routeName"
          [routeDescription]="state.routeDescription"
        />

        @if (state.response; as response) {
          <div class="kpn-spacer-above">
            @if (!response.result) {
              <div>Route not found</div>
            }

            @if (response.result; as page) {
              <div class="kpn-spacer-above">
                <mat-slide-toggle [checked]="service.impact()" (change)="impactChanged($event)"
                  >Impact
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
          </div>
        }

        <kpn-sidebar sidebar />
      </kpn-page>
    }
  `,
  providers: [MonitorRouteChangesPageService, NavService],
  standalone: true,
  imports: [
    MatSlideToggleModule,
    MonitorChangesComponent,
    MonitorRoutePageHeaderComponent,
    PageComponent,
    PaginatorComponent,
    SidebarComponent,
  ],
})
export class MonitorRouteChangesPageComponent {
  protected readonly service = inject(MonitorRouteChangesPageService);

  impactChanged(event: MatSlideToggleChange) {
    this.service.impactChanged(event.checked);
  }

  pageChanged(pageIndex: number) {
    window.scroll(0, 0);
    this.service.pageChanged(pageIndex);
  }
}
