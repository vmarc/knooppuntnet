import { signal } from '@angular/core';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class TryoutPanelsService {
  private readonly _sidebar = signal<string>('menu');
  readonly sidebar = this._sidebar.asReadonly();

  gotoMenu(): void {
    this._sidebar.set('menu');
  }

  gotoSearch(): void {
    this._sidebar.set('search');
    console.log('TryoutPanelsService gotoSearch()');
  }

  gotoPlanner(): void {
    this._sidebar.set('planner');
  }

  gotoConfiguration(): void {
    this._sidebar.set('configuration');
  }

  gotoAnalysis(): void {
    this._sidebar.set('analysis');
  }

  gotoMonitor(): void {
    this._sidebar.set('monitor');
  }
}
