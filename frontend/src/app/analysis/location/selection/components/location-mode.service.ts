import { Signal } from '@angular/core';
import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { WritableSignal } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { BrowserStorageService } from '@app/services';
import { LocationSelectionMode } from './location-selection-mode';

@Injectable()
export class LocationModeService {
  private readonly browserStorageService = inject(BrowserStorageService);
  private readonly localStorageKey = 'location-mode';

  private readonly _mode: WritableSignal<LocationSelectionMode>;
  readonly mode: Signal<LocationSelectionMode>;
  readonly isModeName = computed(() => this.mode() === 'name');
  readonly isModeTree = computed(() => this.mode() === 'tree');

  constructor() {
    let initialMode: LocationSelectionMode = 'name';
    const previousMode = this.browserStorageService.get(this.localStorageKey);
    if (previousMode === null) {
      this.browserStorageService.set(this.localStorageKey, initialMode);
    } else if (previousMode === 'name' || previousMode === 'tree') {
      initialMode = previousMode;
    }
    this._mode = signal<LocationSelectionMode>(initialMode);
    this.mode = this._mode.asReadonly();
  }

  setMode(mode: LocationSelectionMode) {
    this.browserStorageService.set(this.localStorageKey, mode);
    this._mode.set(mode);
  }
}
