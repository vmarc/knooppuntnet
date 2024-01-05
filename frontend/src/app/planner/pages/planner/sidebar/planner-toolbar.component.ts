import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { PageWidth } from '@app/components/shared';
import { PageWidthService } from '@app/components/shared';
import { map } from 'rxjs/operators';
import { NetworkTypeSelectorComponent } from './network-type-selector.component';
import { PlanActionsComponent } from './plan-actions.component';

@Component({
  selector: 'kpn-planner-toolbar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="toolbar">
      <kpn-plan-actions />
      @if (showNetworkTypeSelector$ | async) {
        <kpn-network-type-selector />
      }
    </div>
  `,
  styles: `
    .toolbar {
      display: flex;
      align-items: center;
    }
  `,
  standalone: true,
  imports: [AsyncPipe, NetworkTypeSelectorComponent, PlanActionsComponent],
})
export class PlannerToolbarComponent {
  private readonly pageWidthService = inject(PageWidthService);

  protected showNetworkTypeSelector$ = this.pageWidthService.current$.pipe(
    map((pageWidth) => pageWidth === PageWidth.veryLarge)
  );
}
