import { inject } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { PageWidthService } from '@app/components/shared';
import { RouteTypeSelectorComponent } from './route-type-selector.component';
import { PlanActionsComponent } from './plan-actions.component';

@Component({
  selector: 'kpn-planner-toolbar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="toolbar">
      <kpn-plan-actions />
      @if (showRouteTypeSelector()) {
        <kpn-route-type-selector />
      }
    </div>
  `,
  styles: `
    .toolbar {
      display: flex;
      align-items: center;
    }
  `,
  imports: [RouteTypeSelectorComponent, PlanActionsComponent],
})
export class PlannerToolbarComponent {
  private readonly pageWidthService = inject(PageWidthService);
  protected showRouteTypeSelector = this.pageWidthService.isVeryLarge;
}
