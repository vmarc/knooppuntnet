import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorRouteUpdate } from '@api/common/monitor/monitor-route-update';
import { Timestamp } from '@api/custom';
import { NavService } from '@app/components/shared';
import { from } from 'rxjs';
import { MonitorWebsocketService } from '../../monitor-websocket.service';
import { MonitorService } from '../../monitor.service';
import { MonitorRouteGpxState } from './monitor-route-gpx.state';
import { initialState } from './monitor-route-gpx.state';

@Injectable()
export class MonitorRouteGpxService {
  private readonly _state = signal<MonitorRouteGpxState>(initialState);
  readonly state = this._state.asReadonly();

  constructor(
    private navService: NavService,
    private monitorService: MonitorService,
    private monitorWebsocketService: MonitorWebsocketService,
  ) {
    const groupName = this.navService.param('groupName');
    const routeName = this.navService.param('routeName');
    const subRelationId = this.navService.queryParam('sub-relation-id');
    const groupLink = `/monitor/groups/${groupName}`;
    const routeLink = `/monitor/groups/${groupName}/routes/${routeName}`;
    this._state.set({
      ...initialState,
      groupName,
      routeName,
      subRelationId,
      groupLink,
      routeLink,
    });

    this.monitorService
      .routeGpx(groupName, routeName, subRelationId)
      .subscribe((response) => {
        this._state.update((state) => ({
          ...state,
          response,
        }));
      });
  }

  save(file: File, referenceTimestamp: Timestamp): void {
    const groupName = this.state().groupName;
    const routeName = this.state().routeName;
    const relationId = this.state().subRelationId;
    const referenceGpxPromise = file.text();
    from(referenceGpxPromise).subscribe((referenceGpx) => {
      const command: MonitorRouteUpdate = {
        action: 'gpx-upload',
        groupName,
        routeName,
        referenceType: 'multi-gpx',
        relationId: +relationId,
        referenceTimestamp: referenceTimestamp,
        referenceFilename: file.name,
        referenceGpx: referenceGpx,
      };
      this.monitorWebsocketService.reset();
      this.monitorWebsocketService.sendCommand(command);
    });
  }

  delete(): void {
    const groupName = this.state().groupName;
    const routeName = this.state().routeName;
    const relationId = this.state().subRelationId;

    const command: MonitorRouteUpdate = {
      action: 'gpx-delete',
      groupName,
      routeName,
      referenceType: 'multi-gpx',
      relationId: +relationId,
    };

    this.monitorWebsocketService.reset();
    this.monitorWebsocketService.sendCommand(command);
  }
}
