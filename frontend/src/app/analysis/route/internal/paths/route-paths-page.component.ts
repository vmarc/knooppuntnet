import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RoutePath } from '@api/common/route/route-path';
import { RoutePathListComponent } from '@app/shared/components/route/paths/route-path-list.component';
import { RoutePathsPageService } from './route-paths-page.service';
import { RouterService } from '@app/shared/services/router.service';

@Component({
  selector: 'ui-route-paths-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (response.hasValue()) {
      <ui-route-path-list [paths]="paths()" (selectChange)="selectPath($event)" />
    }
  `,
  providers: [RoutePathsPageService, RouterService],
  imports: [RoutePathListComponent],
})
export class RoutePathsPageComponent {
  private readonly service = inject(RoutePathsPageService);
  protected readonly response = this.service.response;
  protected readonly paths = () => this.response.value()?.result?.paths;

  selectPath(path: RoutePath) {
    // TODO redesign - this.service.selectPath(path);
  }
}
