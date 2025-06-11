import { input } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Country } from '@api/common/country';
import { RouteType } from '@api/common/route-type';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { Translations } from '@app/shared/i18n/translations';
import { RouterService } from '@app/shared/services/router.service';

@Component({
  selector: 'ui-analysis-country-link',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <li>
      <a [routerLink]="link()">
        <span>{{ countryName() }}</span>
      </a>
    </li>
  `,
  providers: [RouterService],
  imports: [RouterLink],
})
export class AnalysisCountryLinkComponent {
  private readonly analysisStrategyService = inject(AnalysisStrategyService);

  readonly routeType = input.required<RouteType>();
  readonly country = input.required<Country>();

  protected readonly countryName = computed(() => Translations.get(`country.${this.country()}`));

  protected readonly link = computed(() => {
    const suffix = this.analysisStrategyService.strategy() === 'network' ? '/networks' : '';
    return `/analysis/${this.routeType()}/${this.country()}${suffix}`;
  });
}
