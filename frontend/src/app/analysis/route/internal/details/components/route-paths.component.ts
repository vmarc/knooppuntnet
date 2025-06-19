import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RoutePathComponent } from '@app/analysis/route/internal/details/components/route-path.component';
import { RouteDetailsPageService } from '@app/analysis/route/internal/details/route-details-page.service';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { RouterService } from '@app/shared/services/router.service';

@Component({
  selector: 'ui-route-paths',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p>
      <span i18n="@@route.paths.title">Paths</span>
    </p>
    <ui-list>
      @for (path of paths(); track path.id) {
        <ui-list-item>
          <ui-route-path [path]="path" />
        </ui-list-item>
      }
    </ui-list>
  `,
  providers: [RouterService],
  imports: [ListComponent, ListItemComponent, RoutePathComponent],
})
export class RoutePathsComponent {
  private readonly service = inject(RouteDetailsPageService);
  protected readonly paths = computed(() => this.service.response()?.result?.data.paths);
}
