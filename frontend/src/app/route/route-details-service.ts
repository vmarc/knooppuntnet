import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorReferenceType } from '@api/common/monitor/monitor-reference-type';

@Injectable()
export class RouteDetailsService {
  private readonly _groupName = signal<string | undefined>(undefined);
  private readonly _routeName = signal<string | undefined>(undefined);
  private readonly _referenceType = signal<MonitorReferenceType | undefined>(undefined);

  readonly groupName = this._groupName.asReadonly();
  readonly routeName = this._routeName.asReadonly();
  readonly referenceType = this._referenceType.asReadonly();

  update(groupName: string, routeName: string, referenceType: MonitorReferenceType | undefined) {
    this._groupName.set(groupName);
    this._routeName.set(routeName);
    this._referenceType.set(referenceType);
  }
}
