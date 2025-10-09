import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkFactRefs } from '@api/common/subset/network-fact-refs';
import { SubsetFactDetailsPage } from '@api/common/subset/subset-fact-details-page';
import { FactDefinition } from '@app/analysis/fact/components/facts';
import { SubsetFactDetailPanelFactComponent } from './subset-fact-detail-panel-fact.component';

@Component({
  selector: 'ui-subset-fact-detail-panel',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="sideline">
      @for (ref of networkFactRefs().factRefs; track ref) {
        <ui-subset-fact-detail-panel-fact [fact]="fact()" [page]="page()" [factRef]="ref" />
      }
    </div>
  `,
  styleUrl: '../subset-fact-details-page.component.scss',
  imports: [SubsetFactDetailPanelFactComponent],
})
export class SubsetFactDetailPanelComponent {
  readonly page = input.required<SubsetFactDetailsPage>();
  readonly fact = input.required<FactDefinition>();
  readonly networkFactRefs = input.required<NetworkFactRefs>();
}
