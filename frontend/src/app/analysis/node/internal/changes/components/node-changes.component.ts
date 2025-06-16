import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ChangesComponent } from '@app/analysis/components/changes/changes.component';
import { NodeChangeComponent } from '@app/analysis/node/internal/changes/components/node-change.component';
import { ItemComponent } from '@app/shared/components/items/item.component';
import { ItemsComponent } from '@app/shared/components/items/items.component';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { RouterService } from '@app/shared/services/router.service';
import { NodeChangesPageService } from '../node-changes-page.service';

@Component({
  selector: 'ui-node-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <p>
        <ui-situation-on [timestamp]="service.response().situationOn" />
      </p>
      <ui-changes
        [impact]="service.impact()"
        [pageSize]="service.pageSize()"
        [pageIndex]="service.pageIndex()"
        (impactChange)="onImpactChange($event)"
        (pageSizeChange)="onPageSizeChange($event)"
        (pageIndexChange)="onPageIndexChange($event)"
        [totalCount]="service.response().result.totalCount"
        [changeCount]="service.response().result.changes.length"
      >
        <ui-items>
          @for (nodeChangeInfo of service.response().result.changes; track nodeChangeInfo) {
            <ui-item [index]="nodeChangeInfo.rowIndex">
              <ui-node-change [nodeChangeInfo]="nodeChangeInfo" />
            </ui-item>
          }
        </ui-items>
      </ui-changes>
    </div>
  `,
  providers: [RouterService],
  imports: [
    ChangesComponent,
    ItemComponent,
    ItemsComponent,
    SituationOnComponent,
    NodeChangeComponent,
  ],
})
export class NodeChangesComponent {
  protected readonly service = inject(NodeChangesPageService);

  onImpactChange(impact: boolean): void {
    this.service.updateImpact(impact);
  }

  onPageSizeChange(pageSize: number): void {
    this.service.updatePageSize(pageSize);
  }

  onPageIndexChange(pageIndex: number): void {
    this.service.updatePageIndex(pageIndex);
  }
}
