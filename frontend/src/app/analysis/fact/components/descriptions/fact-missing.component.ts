import { computed } from '@angular/core';
import { input } from '@angular/core';
import { Component } from '@angular/core';
import { FactInfo } from '@app/analysis/fact/components/fact-info';

@Component({
  selector: 'ui-fact-missing',
  standalone: true,
  template: `<p i18n="@@fact.description-missing">{{ fact() }} description missing!!</p>`,
})
export class FactMissingComponent {
  readonly factInfo = input.required<FactInfo>();
  protected readonly fact = computed(() => this.factInfo().fact);
}
