import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RoutePath } from '@api/common/route/route-path';
import { RoutePathListComponent } from '@app/shared/components/route/paths/route-path-list.component';
import { RoutePathsPageService } from './route-paths-page.service';

@Component({
  selector: 'ui-route-paths-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (response()) {
      <ui-route-path-list [paths]="paths()" (selectChange)="selectPath($event)" />
    }
  `,
  providers: [RoutePathsPageService],
  imports: [RoutePathListComponent],
})
export class RoutePathsPageComponent implements OnInit, OnDestroy {
  private readonly service = inject(RoutePathsPageService);
  protected readonly response = this.service.response;
  protected readonly paths = () => this.response()?.result?.paths;

  ngOnInit(): void {
    this.service.onInit();
  }

  ngOnDestroy(): void {
    this.service.onDestroy();
  }

  selectPath(path: RoutePath) {
    // TODO redesign - this.service.selectPath(path);
  }
}
