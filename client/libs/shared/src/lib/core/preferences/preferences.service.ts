import { computed } from '@angular/core';
import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { actionPreferencesPlanProposed } from './preferences.actions';
import { actionPreferencesShowProposed } from './preferences.actions';
import { actionPreferencesShowOptions } from './preferences.actions';
import { actionPreferencesShowLegend } from './preferences.actions';
import { actionPreferencesShowAppearanceOptions } from './preferences.actions';
import { actionPreferencesImpact } from './preferences.actions';
import { actionPreferencesPageSize } from './preferences.actions';
import { actionPreferencesExtraLayers } from './preferences.actions';
import { actionPreferencesInstructions } from './preferences.actions';
import { actionPreferencesNetworkType } from './preferences.actions';
import { actionPreferencesAnalysisStrategy } from './preferences.actions';
import { selectPreferencesState } from './preferences.selectors';
import { AnalysisStrategy } from './preferences.state';

@Injectable()
export class PreferencesService {
  private _state = this.store.selectSignal(selectPreferencesState);

  readonly strategy = computed(() => {
    return this._state().strategy;
  });

  readonly networkType = computed(() => {
    return this._state().networkType;
  });

  readonly instructions = computed(() => {
    return this._state().instructions;
  });

  readonly extraLayers = computed(() => {
    return this._state().extraLayers;
  });

  readonly pageSize = computed(() => {
    return this._state().pageSize;
  });

  readonly impact = computed(() => {
    return this._state().impact;
  });

  readonly showAppearanceOptions = computed(() => {
    return this._state().showAppearanceOptions;
  });

  readonly showLegend = computed(() => {
    return this._state().showLegend;
  });

  readonly showOptions = computed(() => {
    return this._state().showOptions;
  });

  readonly showProposed = computed(() => {
    return this._state().showProposed;
  });

  readonly planProposed = computed(() => {
    return this._state().planProposed;
  });

  constructor(private store: Store) {}

  setStrategy(strategy: AnalysisStrategy): void {
    this.store.dispatch(actionPreferencesAnalysisStrategy({ strategy }));
  }

  setNetworkType(networkType: string): void {
    this.store.dispatch(actionPreferencesNetworkType({ networkType }));
  }

  setInstructions(instructions: boolean): void {
    this.store.dispatch(actionPreferencesInstructions({ instructions }));
  }

  setExtraLayers(extraLayers: boolean): void {
    this.store.dispatch(actionPreferencesExtraLayers({ extraLayers }));
  }

  setpageSize(pageSize: number): void {
    this.store.dispatch(actionPreferencesPageSize({ pageSize }));
  }

  setImpact(impact: boolean): void {
    this.store.dispatch(actionPreferencesImpact({ impact }));
  }

  setShowAppearanceOptions(value: boolean) {
    this.store.dispatch(actionPreferencesShowAppearanceOptions({ value }));
  }

  setShowLegend(value: boolean) {
    this.store.dispatch(actionPreferencesShowLegend({ value }));
  }

  setShowOptions(value: boolean) {
    this.store.dispatch(actionPreferencesShowOptions({ value }));
  }

  setshowProposed(value: boolean) {
    this.store.dispatch(actionPreferencesShowProposed({ value }));
  }

  setPlanProposed(value: boolean) {
    this.store.dispatch(actionPreferencesPlanProposed({ value }));
  }
}
