import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { DividerComponent } from '@app/shared/components/divider.component';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { ExploreService } from './explore.service';
import { ScopeIconComponent } from './scope-icon.component';

@Component({
  selector: 'ui-explore-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (routes().length > 0) {
      <ui-divider />
      <div>
        <span>Routes</span>
        <span class="kpn-brackets">{{ routes().length }}</span>
      </div>
      <div>
        <ui-list>
          @for (route of routes(); track route.routeId) {
            <ui-list-item [selected]="false">
              <div class="kpn-line">
                <ui-scope-icon [scope]="route.scope" />
                <span>{{ route.name }}</span>
                <span>({{ route.routeId }})</span>
              </div>
            </ui-list-item>
          }
        </ui-list>
      </div>
    }
  `,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    DividerComponent,
    ScopeIconComponent,
    ListComponent,
    ListItemComponent,
  ],
})
export class ExploreRoutesComponent {
  private readonly exploreService = inject(ExploreService);
  readonly routes = this.exploreService.routes;
}
