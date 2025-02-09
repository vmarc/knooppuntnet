import { computed } from '@angular/core';
import { Signal } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { AnalysisStrategy } from '@app/shared/core/preferences/analysis-strategy';
import { State } from '@app/state/state';
import { RouterService } from '@app/shared/services/router.service';

@Injectable()
export class AnalysisStrategyService {
  private readonly state = inject(State);
  private readonly routerService = inject(RouterService);

  readonly strategy = this.state.preferences.strategy;

  init(): void {
    const strategyQueryParam = this.routerService.queryParam('strategy');
    if (strategyQueryParam === 'location') {
      this.state.preferences.updateStrategy('location');
    } else if (strategyQueryParam === 'network') {
      this.state.preferences.updateStrategy('network');
    } else {
      this.routerService.updateQueryParams({ strategy: this.state.preferences.strategy() });
    }
  }

  setStrategy(strategy: AnalysisStrategy): void {
    this.state.preferences.updateStrategy(strategy);
    this.routerService.updateQueryParams({ strategy });
  }

  link(routeType: string, country: string): Signal<string> {
    return computed(() => {
      return (
        `/analysis/${routeType}/${country}` +
        (this.state.preferences.strategy() === 'network' ? '/networks' : '')
      );
    });
  }
}
