import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RoutePath } from '@api/common/route/route-path';
import { RoutePathComponent } from '@app/analysis/route/internal/paths/components/route-path.component';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { RoutePathsPageService } from './route-paths-page.service';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { RoutePageHeaderComponent } from '../components/route-page-header.component';

@Component({
  selector: 'ui-route-segments-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <div class="kpn-small-spacer-above kpn-small-spacer-below">
        <ui-route-page-header pageName="segments" />
      </div>
      <div>
        <button nz-button (click)="zoomToFitRoute()">
          <nz-icon nzType="fullscreen-exit" />
          <span>Zoom to fit entire route</span>
        </button>
      </div>
      @if (response()) {
        <ui-list>
          @for (path of paths(); track path.id) {
            <ui-list-item
              [clickable]="true"
              [selected]="selectedSegment()?.id == path.id"
              (click)="onSelectionChange(path)"
            >
              <ui-route-path [path]="path" />
            </ui-list-item>
          }
        </ui-list>
      }
    </ui-page>
  `,
  providers: [RoutePathsPageService, RouterService],
  imports: [
    ListComponent,
    ListItemComponent,
    NzButtonComponent,
    NzIconDirective,
    PageComponent,
    RoutePageHeaderComponent,
    RoutePathComponent,
  ],
})
export class RoutePathsPageComponent implements OnInit {
  private readonly service = inject(RoutePathsPageService);
  protected readonly response = computed(() => this.service.response());
  protected readonly paths = computed(() => this.response().result.paths);
  protected readonly selectedSegment = computed(() => this.service.selectedSegment());

  ngOnInit(): void {
    this.service.onInit();
  }

  onSelectionChange(routePath: RoutePath): void {
    // TODO redesign - this.service.selectSegment(routePath);
  }

  zoomToFitRoute(): void {
    this.service.selectSegment(undefined);
  }
}
