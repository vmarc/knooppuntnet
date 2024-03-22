import { computed } from '@angular/core';
import { Signal } from '@angular/core';
import { WritableSignal } from '@angular/core';
import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { BrowserStorageService } from '@app/services';
import { initialPreferencesState } from './preferences.state';
import { PreferencesState } from './preferences.state';
import { AnalysisStrategy } from './preferences.state';

@Injectable()
export class PreferencesService {
  private readonly browserStorageService = inject(BrowserStorageService);

  private readonly preferences: WritableSignal<PreferencesState>;

  readonly strategy: Signal<AnalysisStrategy>;
  readonly networkType: Signal<string>;
  readonly instructions: Signal<boolean>;
  // TODO SIGNAL not used anymore? re-introduce?
  readonly extraLayers: Signal<boolean>;
  readonly pageSize: Signal<number>;
  readonly impact: Signal<boolean>;
  readonly showAppearanceOptions: Signal<boolean>;
  readonly showLegend: Signal<boolean>;
  readonly showOptions: Signal<boolean>;
  readonly showProposed: Signal<boolean>;
  readonly planProposed: Signal<boolean>;

  constructor() {
    let preferences: PreferencesState = initialPreferencesState;
    const preferencesString = this.browserStorageService.get('preferences');
    if (preferencesString) {
      preferences = JSON.parse(preferencesString);
    }
    this.preferences = signal<PreferencesState>(preferences);

    this.strategy = computed(() => this.preferences().strategy);
    this.networkType = computed(() => this.preferences().networkType);
    this.instructions = computed(() => this.preferences().instructions);
    // TODO SIGNAL not used anymore? re-introduce?
    this.extraLayers = computed(() => this.preferences().extraLayers);
    this.pageSize = computed(() => this.preferences().pageSize);
    this.impact = computed(() => this.preferences().impact);
    this.showAppearanceOptions = computed(() => this.preferences().showAppearanceOptions);
    this.showLegend = computed(() => this.preferences().showLegend);
    this.showOptions = computed(() => this.preferences().showOptions);
    this.showProposed = computed(() => this.preferences().showProposed);
    this.planProposed = computed(() => this.preferences().planProposed);
  }

  setStrategy(strategy: AnalysisStrategy): void {
    this.update({
      ...this.preferences(),
      strategy,
    });
  }

  setNetworkType(networkType: string): void {
    this.update({
      ...this.preferences(),
      networkType,
    });
  }

  setInstructions(instructions: boolean): void {
    this.update({
      ...this.preferences(),
      instructions,
    });
  }

  setExtraLayers(extraLayers: boolean): void {
    this.update({
      ...this.preferences(),
      extraLayers,
    });
  }

  setPageSize(pageSize: number): void {
    this.update({
      ...this.preferences(),
      pageSize,
    });
  }

  setImpact(impact: boolean): void {
    this.update({
      ...this.preferences(),
      impact,
    });
  }

  setShowAppearanceOptions(showAppearanceOptions: boolean) {
    this.update({
      ...this.preferences(),
      showAppearanceOptions,
    });
  }

  setShowLegend(showLegend: boolean) {
    this.update({
      ...this.preferences(),
      showLegend,
    });
  }

  setShowOptions(showOptions: boolean) {
    this.update({
      ...this.preferences(),
      showOptions,
    });
  }

  setShowProposed(showProposed: boolean) {
    this.update({
      ...this.preferences(),
      showProposed,
    });
  }

  setPlanProposed(planProposed: boolean) {
    this.update({
      ...this.preferences(),
      planProposed,
    });
  }

  private update(preferences: PreferencesState): void {
    this.preferences.set(preferences);
    const json = JSON.stringify(preferences);
    this.browserStorageService.set('preferences', json);
  }
}
