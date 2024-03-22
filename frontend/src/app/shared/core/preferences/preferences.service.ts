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

  private readonly _strategy: WritableSignal<AnalysisStrategy>;
  private readonly _networkType: WritableSignal<string>;
  private readonly _instructions: WritableSignal<boolean>;
  private readonly _extraLayers: WritableSignal<boolean>;
  private readonly _pageSize: WritableSignal<number>;
  private readonly _impact: WritableSignal<boolean>;
  private readonly _showAppearanceOptions: WritableSignal<boolean>;
  private readonly _showLegend: WritableSignal<boolean>;
  private readonly _showOptions: WritableSignal<boolean>;
  private readonly _showProposed: WritableSignal<boolean>;
  private readonly _planProposed: WritableSignal<boolean>;

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
    this._strategy = signal<AnalysisStrategy>(preferences.strategy);
    this._networkType = signal<string>(preferences.networkType);
    this._instructions = signal<boolean>(preferences.instructions);
    this._extraLayers = signal<boolean>(preferences.extraLayers);
    this._pageSize = signal<number>(preferences.pageSize);
    this._impact = signal<boolean>(preferences.impact);
    this._showAppearanceOptions = signal<boolean>(preferences.showAppearanceOptions);
    this._showLegend = signal<boolean>(preferences.showLegend);
    this._showOptions = signal<boolean>(preferences.showOptions);
    this._showProposed = signal<boolean>(preferences.showProposed);
    this._planProposed = signal<boolean>(preferences.planProposed);

    this.strategy = this._strategy.asReadonly();
    this.networkType = this._networkType.asReadonly();
    this.instructions = this._instructions.asReadonly();
    // TODO SIGNAL not used anymore? re-introduce?
    this.extraLayers = this._extraLayers.asReadonly();
    this.pageSize = this._pageSize.asReadonly();
    this.impact = this._impact.asReadonly();
    this.showAppearanceOptions = this._showAppearanceOptions.asReadonly();
    this.showLegend = this._showLegend.asReadonly();
    this.showOptions = this._showOptions.asReadonly();
    this.showProposed = this._showProposed.asReadonly();
    this.planProposed = this._planProposed.asReadonly();
  }

  setStrategy(strategy: AnalysisStrategy): void {
    this._strategy.set(strategy);
    this.updateLocalStorage();
  }

  setNetworkType(networkType: string): void {
    this._networkType.set(networkType);
    this.updateLocalStorage();
  }

  setInstructions(instructions: boolean): void {
    this._instructions.set(instructions);
    this.updateLocalStorage();
  }

  setExtraLayers(extraLayers: boolean): void {
    this._extraLayers.set(extraLayers);
    this.updateLocalStorage();
  }

  setPageSize(pageSize: number): void {
    this._pageSize.set(pageSize);
    this.updateLocalStorage();
  }

  setImpact(impact: boolean): void {
    this._impact.set(impact);
    this.updateLocalStorage();
  }

  setShowAppearanceOptions(value: boolean) {
    this._showAppearanceOptions.set(value);
    this.updateLocalStorage();
  }

  setShowLegend(value: boolean) {
    this._showLegend.set(value);
    this.updateLocalStorage();
  }

  setShowOptions(value: boolean) {
    this._showOptions.set(value);
    this.updateLocalStorage();
  }

  setShowProposed(value: boolean) {
    this._showProposed.set(value);
    this.updateLocalStorage();
  }

  setPlanProposed(value: boolean) {
    this._planProposed.set(value);
    this.updateLocalStorage();
  }

  private updateLocalStorage(): void {
    const preferences: PreferencesState = {
      strategy: this.strategy(),
      networkType: this.networkType(),
      instructions: this.instructions(),
      extraLayers: this.extraLayers(),
      pageSize: this.pageSize(),
      impact: this.impact(),
      showAppearanceOptions: this.showAppearanceOptions(),
      showLegend: this.showLegend(),
      showOptions: this.showOptions(),
      showProposed: this.showProposed(),
      planProposed: this.planProposed(),
    };
    const json = JSON.stringify(preferences);
    this.browserStorageService.set('preferences', json);
  }
}
