import { effect } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { BrowserStorageService } from '@app/services';
import { State } from '@app/state';
import { Preferences } from './preferences';

@Injectable()
export class PreferencesService {
  private readonly state = inject(State);
  private readonly browserStorageService = inject(BrowserStorageService);

  constructor() {
    const preferencesString = this.browserStorageService.get('preferences');
    if (preferencesString) {
      const preferences: Preferences = JSON.parse(preferencesString);
      if (preferences?.strategy) {
        this.state.preferences.updateStrategy(preferences.strategy);
      }
      if (preferences?.routeType) {
        this.state.preferences.updateRouteType(preferences.routeType);
      }
      if (preferences?.extraLayers) {
        this.state.preferences.updateExtraLayers(preferences.extraLayers);
      }
      if (preferences?.pageSize) {
        this.state.preferences.updatePageSize(preferences.pageSize);
      }
      if (preferences?.impact) {
        this.state.preferences.updateImpact(preferences.impact);
      }
      if (preferences?.showLegend) {
        this.state.preferences.updateShowLegend(preferences.showLegend);
      }
      if (preferences?.showOptions) {
        this.state.preferences.updateShowOptions(preferences.showOptions);
      }
      if (preferences?.showProposed) {
        this.state.preferences.updateShowProposed(preferences.showProposed);
      }
      if (preferences?.planProposed) {
        this.state.preferences.updatePlanProposed(preferences.planProposed);
      }
    }

    effect(() => {
      const preferences: Preferences = {
        strategy: this.state.preferences.strategy(),
        routeType: this.state.preferences.routeType(),
        extraLayers: this.state.preferences.extraLayers(),
        pageSize: this.state.preferences.pageSize(),
        impact: this.state.preferences.impact(),
        showLegend: this.state.preferences.showLegend(),
        showOptions: this.state.preferences.showOptions(),
        showProposed: this.state.preferences.showProposed(),
        planProposed: this.state.preferences.planProposed(),
      };
      const json = JSON.stringify(preferences);
      this.browserStorageService.set('preferences', json);
    });
  }
}
