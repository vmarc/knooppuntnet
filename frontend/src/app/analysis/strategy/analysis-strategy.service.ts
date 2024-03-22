import { computed } from '@angular/core';
import { Signal } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { PreferencesService } from '@app/core';
import { AnalysisStrategy } from '@app/core';

@Injectable()
export class AnalysisStrategyService {
  private readonly preverencesService = inject(PreferencesService);

  link(networkType: string, country: string): Signal<string> {
    return computed(() => {
      return (
        `/analysis/${networkType}/${country}` +
        (this.preverencesService.strategy() === AnalysisStrategy.network ? '/networks' : '')
      );
    });
  }
}
