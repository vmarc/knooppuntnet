import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Fact } from '@api/common/fact';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';
import { IconInvestigateComponent } from '@app/shared/components/icon/icon-investigate.component';
import { FactNameComponent } from '../../fact/components/fact-name.component';

@Component({
  selector: 'ui-fact-comma-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (hasFacts()) {
      <div class="kpn-detail kpn-line">
        {{ title() }}:&nbsp;
        <div class="kpn-comma-list">
          @for (fact of facts(); track $index) {
            <span>
              <ui-fact-name [fact]="fact" />
            </span>
          }
        </div>
        @if (icon() === 'happy') {
          <ui-icon-happy />
        }
        @if (icon() === 'investigate') {
          <ui-icon-investigate />
        }
      </div>
    }
  `,
  imports: [FactNameComponent, IconHappyComponent, IconInvestigateComponent],
})
export class FactCommaListComponent {
  readonly title = input.required<string>();
  readonly facts = input.required<Fact[]>();
  readonly icon = input.required<string>();

  hasFacts(): boolean {
    return this.facts() && this.facts().length > 0;
  }
}
