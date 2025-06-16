import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { ChangeNetworkAnalysisSummaryComponent } from '@app/analysis/components/change-set/change-network-analysis-summary.component';
import { ChangeLocationAnalysisSummaryComponent } from '@app/analysis/components/change-set/change-location-analysis-summary.component';
import { ChangesComponent } from '@app/analysis/components/changes/changes.component';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { SubsetChangesPageService } from '../subset-changes-page.service';

@Component({
  selector: 'ui-subset-changes',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <ui-changes [service]="service">
      @for (changeSet of service.response().result.changes; track changeSet.rowIndex) {
        <ui-list-item [selected]="false">
          @if (changeSet.network) {
            <ui-change-network-analysis-summary [changeSet]="changeSet" />
          }
          @if (changeSet.location) {
            <ui-change-location-analysis-summary [changeSet]="changeSet" />
          }
        </ui-list-item>
      }
    </ui-changes>
  `,
  imports: [
    ChangeLocationAnalysisSummaryComponent,
    ChangeNetworkAnalysisSummaryComponent,
    ChangesComponent,
    ListItemComponent,
  ],
})
export class SubsetChangesComponent {
  protected readonly service = inject(SubsetChangesPageService);
}
