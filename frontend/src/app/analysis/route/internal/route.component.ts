import { effect } from '@angular/core';
import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { RoutePageHeaderComponent } from '@app/analysis/route/internal/components/route-page-header.component';
import { RouteService } from '@app/analysis/route/internal/route.service';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';

@Component({
  selector: 'ui-route',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-route-page-header />
      <router-outlet />
    </ui-page>
  `,
  imports: [RouterOutlet, PageComponent, RoutePageHeaderComponent],
  providers: [RouterService],
})
export class RouteComponent {
  private routeService = inject(RouteService);

  readonly routeId = input.required<string>();

  constructor() {
    effect(() => this.routeService.updateRouteId(+this.routeId()));
  }
}
