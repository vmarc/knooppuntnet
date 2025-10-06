import { computed, Signal } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Fact } from '@api/common/fact';
import { FactLevel } from '@api/common/fact-level';
import { FactTermComponent } from '@app/analysis/fact/components/fact-term.component';
import { Facts } from '@app/analysis/fact/components/facts';

export interface LineFact {
  readonly fact: Fact;
  readonly level: FactLevel;
}

@Component({
  selector: 'ui-facts-line',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="facts">
      @for (fact of facts(); track fact) {
        <ui-fact-term [fact]="fact" />
      }
    </div>
  `,
  styles: `
    .facts {
      display: flex;
      flex-wrap: wrap;
      line-height: 1em;
      gap: 0.5em;
      padding-top: 0.5em;

      :not(:last-child):after {
        content: ',';
      }
    }
  `,
  imports: [FactTermComponent],
})
export class FactsLineComponent {
  readonly facts = input.required<Fact[]>();

  readonly lineFacts = computed(() => {
    return this.facts().map((fact) => {
      const level = Facts.factLevel(fact);
      const lineFact: LineFact = {
        fact,
        level,
      };
      return lineFact;
    });
  });
}
