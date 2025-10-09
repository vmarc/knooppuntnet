import { viewChildren } from '@angular/core';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { SubsetFactDetailsPage } from '@api/common/subset/subset-fact-details-page';
import { Facts } from '@app/analysis/fact/components/facts';
import { SubsetFactDetailPanelComponent } from '@app/analysis/subset/internal/fact-details/components/subset-fact-detail-panel.component';
import { SubsetFactDetailPanelHeaderComponent } from './subset-fact-detail-panel-header.component';
import { ExpandCollapseComponent } from '@app/shared/components/button/expand-collapse.component';
import { NzCollapsePanelComponent } from 'ng-zorro-antd/collapse';
import { NzCollapseComponent } from 'ng-zorro-antd/collapse';

@Component({
  selector: 'ui-subset-fact-details',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (page().networks.length > 0) {
      <ui-expand-collapse (expandAll)="expandAll()" (collapseAll)="collapseAll()" />
      @if (fact(); as fact) {
        <nz-collapse>
          @for (networkFactRefs of page().networks; track networkFactRefs) {
            <nz-collapse-panel [nzHeader]="header">
              <ng-template #header>
                <ui-subset-fact-detail-panel-header
                  [fact]="fact"
                  [networkFactRefs]="networkFactRefs"
                />
              </ng-template>
              <ui-subset-fact-detail-panel
                [page]="page()"
                [fact]="fact"
                [networkFactRefs]="networkFactRefs"
              />
            </nz-collapse-panel>
          }
        </nz-collapse>
      }
    }
  `,
  styleUrl: '../subset-fact-details-page.component.scss',
  imports: [
    ExpandCollapseComponent,
    NzCollapseComponent,
    NzCollapsePanelComponent,
    SubsetFactDetailPanelHeaderComponent,
    SubsetFactDetailPanelComponent,
  ],
})
export class SubsetFactDetailsComponent {
  readonly page = input.required<SubsetFactDetailsPage>();
  protected readonly fact = computed(() => Facts.factDefinitionMap.get(this.page().fact));
  private readonly panels = viewChildren(NzCollapsePanelComponent);

  expandAll(): void {
    this.activeAll(true);
  }

  collapseAll(): void {
    this.activeAll(false);
  }

  private activeAll(active: boolean): void {
    this.panels().forEach((panel) => {
      panel.nzActive = active;
      panel.markForCheck();
    });
  }
}
