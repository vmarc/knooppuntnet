import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkFact } from '@api/common/network-fact';
import { Facts } from '@app/analysis/fact/components/facts';
import { FactLevelComponent } from '@app/analysis/fact/components/fact-level.component';
import { FactNameComponent } from '@app/analysis/fact/components/fact-name.component';
import { NetworkFactActionButtonComponent } from '@app/analysis/network/internal/facts/components/network-fact-action-button.component';

@Component({
  selector: 'ui-network-fact-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      <ui-network-fact-action-button [networkFact]="networkFact()" />
      <ui-fact-name [fact]="networkFact().fact" />
      <span class="kpn-brackets">{{ factCount() }}</span>
      <ui-fact-level [factLevel]="factLevel()" class="level" />
    </div>
  `,
  imports: [FactNameComponent, FactLevelComponent, NetworkFactActionButtonComponent],
})
export class NetworkFactHeaderComponent {
  readonly networkFact = input.required<NetworkFact>();
  protected readonly factLevel = computed(() => Facts.factLevel(this.networkFact().fact));
  protected readonly factCount = computed(() => {
    const fact = this.networkFact();
    if (fact.elements && fact.elements.length > 0) {
      return fact.elements.length;
    }
    if (fact.elementIds && fact.elementIds.length > 0) {
      return fact.elementIds.length;
    }
    if (fact.checks) {
      return fact.checks.length;
    }
    return 0;
  });
}
