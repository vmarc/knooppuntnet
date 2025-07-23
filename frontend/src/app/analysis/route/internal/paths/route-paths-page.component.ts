import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RoutePath } from '@api/common/route/route-path';
import { RoutePathListComponent } from '@app/shared/components/route/paths/route-path-list.component';
import { RoutePathsPageService } from './route-paths-page.service';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { RoutePageHeaderComponent } from '../components/route-page-header.component';

@Component({
  selector: 'ui-route-paths-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-route-page-header pageName="paths" />
      <ui-route-path-list [paths]="paths()" (selectChange)="selectPath($event)" />
    </ui-page>
  `,
  providers: [RoutePathsPageService, RouterService],
  imports: [PageComponent, RoutePageHeaderComponent, RoutePathListComponent],
})
export class RoutePathsPageComponent implements OnInit {
  private readonly service = inject(RoutePathsPageService);
  protected readonly paths = () => this.service.response().result.paths;

  ngOnInit(): void {
    this.service.onInit();
  }

  selectPath(path: RoutePath) {
    // TODO redesign - this.service.selectPath(path);
  }
}
