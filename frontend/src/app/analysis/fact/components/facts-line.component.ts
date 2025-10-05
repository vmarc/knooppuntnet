import { computed, Signal } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Fact } from '@api/common/fact';
import { FactLevel } from '@api/common/fact-level';
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
      @for (lineFact of lineFacts(); track lineFact.fact) {
        @switch (lineFact.level) {
          @case ('error') {
            <span class="color-error">{{ lineFact.fact }}</span>
          }
          @case ('info') {
            <span class="color-info">{{ lineFact.fact }}</span>
          }
          @default {
            <span class="color-other">{{ lineFact.fact }}</span>
          }
        }
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
  imports: [],
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
