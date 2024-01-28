import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Fact } from '@api/custom';
import { FactNameComponent } from '@app/analysis/fact';
import { IconHappyComponent } from '@app/components/shared/icon';
import { IconInvestigateComponent } from '@app/components/shared/icon';

@Component({
  selector: 'kpn-fact-comma-list',
  changeDetection: ChangeDetectionStrategy.OnPush,

  template: `
    @if (hasFacts()) {
      <div class="kpn-detail kpn-line">
        {{ title() }}:&nbsp;
        <div class="kpn-comma-list">
          @for (fact of facts(); track $index) {
            <span>
              <kpn-fact-name [fact]="fact" />
            </span>
          }
        </div>
        @if (icon() === 'happy') {
          <kpn-icon-happy />
        }
        @if (icon() === 'investigate') {
          <kpn-icon-investigate />
        }
      </div>
    }
  `,
  standalone: true,
  imports: [FactNameComponent, IconHappyComponent, IconInvestigateComponent],
})
export class FactCommaListComponent {
  title = input<string | undefined>();
  facts = input<Fact[] | undefined>();
  icon = input<string | undefined>();

  hasFacts(): boolean {
    return this.facts() && this.facts().length > 0;
  }
}
