import { WritableSignal } from '@angular/core';
import { Signal } from '@angular/core';
import { signal } from '@angular/core';

export class MapStateScopes {
  private readonly _scopeInternational: WritableSignal<boolean>;
  private readonly _scopeNational: WritableSignal<boolean>;
  private readonly _scopeRegional: WritableSignal<boolean>;
  private readonly _scopeLocal: WritableSignal<boolean>;
  private readonly _scopeNodeRoutes: WritableSignal<boolean>;

  readonly scopeInternational: Signal<boolean>;
  readonly scopeNational: Signal<boolean>;
  readonly scopeRegional: Signal<boolean>;
  readonly scopeLocal: Signal<boolean>;
  readonly scopeNodeRoutes: Signal<boolean>;

  constructor() {
    // TODO redesign - add initial values based on local storage and query params

    this._scopeInternational = signal<boolean>(true);
    this._scopeNational = signal<boolean>(true);
    this._scopeRegional = signal<boolean>(true);
    this._scopeLocal = signal<boolean>(true);
    this._scopeNodeRoutes = signal<boolean>(true);

    this.scopeInternational = this._scopeInternational.asReadonly();
    this.scopeNational = this._scopeNational.asReadonly();
    this.scopeRegional = this._scopeRegional.asReadonly();
    this.scopeLocal = this._scopeLocal.asReadonly();
    this.scopeNodeRoutes = this._scopeNodeRoutes.asReadonly();
  }

  updateScopeInternational(value: boolean): void {
    this._scopeInternational.set(value);
  }

  updateScopeNational(value: boolean): void {
    this._scopeNational.set(value);
  }

  updateScopeRegional(value: boolean): void {
    this._scopeRegional.set(value);
  }

  updateScopeLocal(value: boolean): void {
    this._scopeLocal.set(value);
  }

  updateScopeNodeRoutes(value: boolean): void {
    this._scopeNodeRoutes.set(value);
  }
}
