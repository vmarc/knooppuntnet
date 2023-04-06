import { ChangeDetectionStrategy, Component } from '@angular/core';
import { PageWidth } from '@app/components/shared/page-width';
import { PageWidthService } from '@app/components/shared/page-width.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'kpn-planner-toolbar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="toolbar">
      <kpn-plan-actions />
      <kpn-network-type-selector *ngIf="showNetworkTypeSelector$ | async" />
    </div>
  `,
  styles: [
    `
      .toolbar {
        display: flex;
        align-items: center;
      }
    `,
  ],
})
export class PlannerToolbarComponent {
  showNetworkTypeSelector$: Observable<boolean>;

  constructor(private pageWidthService: PageWidthService) {
    this.showNetworkTypeSelector$ = pageWidthService.current$.pipe(
      map((pageWidth) => pageWidth === PageWidth.veryLarge)
    );
  }
}
