import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DividerComponent } from '@app/components/shared';
import { ReactiveFormsModule } from '@angular/forms';
import { ListItemComponent } from '@app/components/shared/list';
import { ListComponent } from '@app/components/shared/list';
import { ExploreService } from './explore.service';
import { ScopeIconComponent } from './scope-icon.component';

@Component({
  selector: 'kpn-explore-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (routes().length > 0) {
      <kpn-divider />
      <div>
        <span>Routes</span>
        <span class="kpn-brackets">{{ routes().length }}</span>
      </div>
      <div>
        <kpn-list>
          @for (route of routes(); track route.routeId) {
            <kpn-list-item [selected]="false">
              <div class="kpn-line">
                <kpn-scope-icon [scope]="route.scope" />
                <span>{{ route.name }}</span>
                <span>({{ route.routeId }})</span>
              </div>
            </kpn-list-item>
          }
        </kpn-list>
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
