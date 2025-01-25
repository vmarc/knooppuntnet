import { computed } from '@angular/core';
import { Signal } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { PreferencesService } from '@app/core';
import { AnalysisStrategy } from '@app/core';
import { RouterService } from '../../shared/services/router.service';

@Injectable()
export class AnalysisStrategyService {
  private readonly preferencesService = inject(PreferencesService);
  private readonly routerService = inject(RouterService);

  readonly strategy = this.preferencesService.strategy;

  init(): void {
    const strategyQueryParam = this.routerService.queryParam('strategy');
    if (strategyQueryParam === 'location') {
      this.preferencesService.setStrategy('location');
    } else if (strategyQueryParam === 'network') {
      this.preferencesService.setStrategy('network');
    } else {
      this.routerService.updateQueryParams({ strategy: this.preferencesService.strategy() });
    }
  }

  setStrategy(strategy: AnalysisStrategy): void {
    this.preferencesService.setStrategy(strategy);
    this.routerService.updateQueryParams({ strategy });
  }

  link(routeType: string, country: string): Signal<string> {
    return computed(() => {
      return (
        `/analysis/${routeType}/${country}` +
        (this.preferencesService.strategy() === 'network' ? '/networks' : '')
      );
    });
  }
}
