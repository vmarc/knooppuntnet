import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { NavService } from '@app/shared/components/nav.service';
import { PageComponent } from '@app/shared/components/page/page.component';
import { OldPaginatorComponent } from '@app/shared/components/paginator/old-paginator.component';
import { MonitorChangesComponent } from '../../components/monitor-changes.component';
import { MonitorRoutePageHeaderComponent } from '../components/monitor-route-page-header.component';
import { MonitorRouteChangesPageService } from './monitor-route-changes-page.service';

@Component({
  selector: 'ui-monitor-route-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- work-in-progress -->
    <!-- eslint-disable @angular-eslint/template/i18n -->

    @if (service.changesState(); as state) {
      <ui-page>
        <ui-monitor-route-page-header
          pageName="changes"
          [groupName]="state.groupName"
          [routeName]="state.routeName"
          [routeDescription]="state.routeDescription"
          [memberCount]="999"
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
          </div>
        }
      </ui-page>
    }
  `,
  providers: [MonitorRouteChangesPageService, NavService],
  imports: [
    MatSlideToggleModule,
    MonitorChangesComponent,
    MonitorRoutePageHeaderComponent,
    OldPaginatorComponent,
    PageComponent,
  ],
})
export class MonitorRouteChangesPageComponent {
  readonly service = inject(MonitorRouteChangesPageService);

  impactChanged(event: MatSlideToggleChange) {
    this.service.updateImpact(event.checked);
  }

  pageChanged(pageIndex: number) {
    window.scroll(0, 0);
    this.service.updatePage(pageIndex);
  }
}
