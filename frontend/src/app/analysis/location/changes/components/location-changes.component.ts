import { ChangeDetectionStrategy, Component, inject, input } from '@angular/core';
import { ChangesComponent } from '@app/analysis/components/changes';
import { ItemComponent } from '@app/shared/components/items/item.component';
import { ItemsComponent } from '@app/shared/components/items/items.component';
import { LocationChangesPageService } from '../location-changes-page.service';
import { LocationChangeComponent } from './location-change.component';
import { LocationChangesPage } from '@api/common/location';

@Component({
  selector: 'kpn-location-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-spacer-above">
      <kpn-changes
        [impact]="service.impact()"
        [pageSize]="service.pageSize()"
        [pageIndex]="service.pageIndex()"
        (impactChange)="onImpactChange($event)"
        (pageSizeChange)="onPageSizeChange($event)"
        (pageIndexChange)="onPageIndexChange($event)"
        [totalCount]="page().changesCount"
        [changeCount]="page().changeSets.length"
      >
        <kpn-items>
          @for (changeSet of page().changeSets; track $index) {
            <kpn-item [index]="changeSet.rowIndex">
              <kpn-location-change [changeSet]="changeSet" />
            </kpn-item>
          }
        </kpn-items>
      </kpn-changes>
    </div>
  `,
  imports: [ChangesComponent, ItemComponent, ItemsComponent, LocationChangeComponent],
})
export class LocationChangesComponent {
  protected readonly service = inject(LocationChangesPageService);

  page = input.required<LocationChangesPage>();

  onImpactChange(impact: boolean): void {
    this.service.setImpact(impact);
  }

  onPageSizeChange(pageSize: number): void {
    this.service.setPageSize(pageSize);
  }

  onPageIndexChange(pageIndex: number): void {
    this.service.setPageIndex(pageIndex);
  }
}
