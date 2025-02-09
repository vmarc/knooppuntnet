import { signal } from '@angular/core';
import { RouteType } from '@api/common/route-type';
import { AnalysisStrategy } from '@app/shared/core/preferences/analysis-strategy';

export class PreferencesState {
  private readonly _strategy = signal<AnalysisStrategy>('location');
  private readonly _routeType = signal<RouteType>('hiking');
  // TODO SIGNAL not used anymore? re-introduce?
  private readonly _extraLayers = signal<boolean>(false);
  private readonly _pageSize = signal<number>(25);
  private readonly _impact = signal<boolean>(true);
  private readonly _showLegend = signal<boolean>(true);
  private readonly _showOptions = signal<boolean>(true);
  private readonly _showProposed = signal<boolean>(true);
  private readonly _planProposed = signal<boolean>(false);

  readonly strategy = this._strategy.asReadonly();
  readonly routeType = this._routeType.asReadonly();
  readonly extraLayers = this._extraLayers.asReadonly();
  readonly pageSize = this._pageSize.asReadonly();
  readonly impact = this._impact.asReadonly();
  readonly showLegend = this._showLegend.asReadonly();
  readonly showOptions = this._showOptions.asReadonly();
  readonly showProposed = this._showProposed.asReadonly();
  readonly planProposed = this._planProposed.asReadonly();

  updateStrategy(value: AnalysisStrategy): void {
    this._strategy.set(value);
  }

  updateRouteType(value: RouteType): void {
    this._routeType.set(value);
  }

  updateExtraLayers(value: boolean): void {
    this._extraLayers.set(value);
  }

  updatePageSize(value: number): void {
    this._pageSize.set(value);
  }

  updateImpact(value: boolean): void {
    this._impact.set(value);
  }

  updateShowLegend(value: boolean): void {
    this._showLegend.set(value);
  }

  updateShowOptions(value: boolean): void {
    this._showOptions.set(value);
  }

  updateShowProposed(value: boolean): void {
    this._showProposed.set(value);
  }

  updatePlanProposed(value: boolean): void {
    this._planProposed.set(value);
  }
}
