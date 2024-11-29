import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatNavList } from '@angular/material/list';
import { MatListItem } from '@angular/material/list';
import { DividerComponent } from '@app/components/shared';
import { ReactiveFormsModule } from '@angular/forms';
import { ExploreService } from './explore.service';

@Component({
  selector: 'kpn-explore-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (routes().length > 0) {
      <kpn-divider />
      <div>Routes</div>
      <div>
        <mat-nav-list>
          @for (route of routes(); track route.routeId) {
            <mat-list-item> {{ route.scope }} {{ route.name }} ({{ route.routeId }})</mat-list-item>
          }
        </mat-nav-list>
      </div>
    }
  `,
  imports: [FormsModule, ReactiveFormsModule, MatListItem, MatNavList, DividerComponent],
})
export class ExploreRoutesComponent {
  private readonly exploreService = inject(ExploreService);
  readonly routes = this.exploreService.routes;
}
