import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkFact } from '@api/common/network-fact';
import { ActionButtonNodesComponent } from '@app/analysis/components/action/action-button-nodes.component';
import { ActionButtonRelationsComponent } from '@app/analysis/components/action/action-button-relations.component';
import { ActionButtonRoutesComponent } from '@app/analysis/components/action/action-button-routes.component';
import { ActionButtonWaysComponent } from '@app/analysis/components/action/action-button-ways.component';

@Component({
  selector: 'ui-network-fact-action-button',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let fact = networkFact();
    @if (fact.elements && fact.elementType === 'node') {
      <ui-action-button-nodes [nodeIds]="elementIds()" />
    } @else if (fact.elements && fact.elementType === 'route') {
      <ui-action-button-routes [relationIds]="elementIds()" />
    } @else if (fact.checks && fact.checks.length > 0) {
      <ui-action-button-nodes [nodeIds]="checkIds()" />
    } @else if (fact.elementIds) {
      @switch (fact.elementType) {
        @case ('node') {
          <ui-action-button-nodes [nodeIds]="fact.elementIds" />
        }
        @case ('way') {
          <ui-action-button-ways [wayIds]="fact.elementIds" />
        }
        @case ('relation') {
          <ui-action-button-relations [relationIds]="fact.elementIds" />
        }
        @case ('route') {
          <ui-action-button-routes [relationIds]="fact.elementIds" />
        }
      }
    }
  `,
  imports: [
    ActionButtonNodesComponent,
    ActionButtonRelationsComponent,
    ActionButtonRoutesComponent,
    ActionButtonWaysComponent,
  ],
})
export class NetworkFactActionButtonComponent {
  readonly networkFact = input.required<NetworkFact>();

  protected readonly elementIds = computed(() => {
    return this.networkFact().elements.map((element) => element.id);
  });

  protected readonly checkIds = computed(() => {
    return this.networkFact().checks.map((check) => check.nodeId);
  });
}
