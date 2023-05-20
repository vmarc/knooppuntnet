import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { mergeMap } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { initialMonitorRouteSaveState } from '../../store/monitor.state';
import { MonitorRouteSaveState } from '../../store/monitor.state';
import { MonitorRouteService } from '../monitor-route.service';
import { MonitorRouteParameters } from './monitor-route-parameters';

@Injectable()
export class MonitorRouteSaveDialogService {
  private readonly _state = signal<MonitorRouteSaveState>(
    initialMonitorRouteSaveState
  );
  readonly state = this._state.asReadonly();

  constructor(private monitorRouteService: MonitorRouteService) {}

  init(parameters: MonitorRouteParameters): void {
    if (parameters.mode === 'add') {
      this.initAdd(parameters);
    } else {
      this.initUpdate(parameters);
    }
  }

  private initAdd(parameters: MonitorRouteParameters): void {
    const gpx = parameters.properties.referenceType === 'gpx';
    this._state.update((state) => ({
      ...state,
      saveRouteEnabled: true,
      saveRouteStatus: 'busy',
      uploadGpxEnabled: gpx,
      analyzeEnabled: gpx,
    }));

    if (gpx) {
      this.saveRoute(parameters)
        .pipe(
          mergeMap(() => this.upload(parameters)),
          mergeMap(() => this.analyze(parameters))
        )
        .subscribe();
    } else {
      this.saveRoute(parameters).subscribe();
    }
  }

  private initUpdate(parameters: MonitorRouteParameters): void {
    const referenceTypeGpx =
      parameters.properties.referenceType === 'gpx' ||
      parameters.properties.referenceType === 'multi-gpx';

    const gpx =
      (parameters.properties.referenceType === 'gpx' ||
        parameters.properties.referenceType === 'multi-gpx') &&
      parameters.properties.referenceFileChanged;

    this._state.update((state) => {
      return {
        ...state,
        saveRouteEnabled: true,
        saveRouteStatus: 'busy',
        uploadGpxEnabled: gpx,
        analyzeEnabled: gpx,
      };
    });

    const groupName = parameters.initialProperties.groupName;
    const properties = parameters.properties;

    this.monitorRouteService
      .routeUpdate(groupName, parameters.initialProperties.name, properties)
      .subscribe((response) => {
        if (referenceTypeGpx && properties.referenceFileChanged) {
          // TODO return actionMonitorRouteUploadInit(parameters);
        }
        // TODO return actionMonitorRouteSaved(response);
      });
  }

  private saveRoute(parameters: MonitorRouteParameters) {
    const groupName = parameters.initialProperties.groupName;
    const properties = parameters.properties;
    this._state.update((state) => ({
      ...state,
      saveRouteStatus: 'busy',
    }));

    return this.monitorRouteService.routeAdd(groupName, properties).pipe(
      tap((routeAddResponse) => {
        const done = !(
          this.state().uploadGpxEnabled || this.state().analyzeEnabled
        );
        this._state.update((state) => ({
          ...state,
          saveRouteStatus: 'done',
          errors: routeAddResponse.result?.errors,
          exception: routeAddResponse.result?.exception,
          done,
        }));
      })
    );
  }

  private upload(parameters: MonitorRouteParameters) {
    this._state.update((state) => ({
      ...state,
      uploadGpxStatus: 'busy',
    }));
    const relationId = 0;
    return this.monitorRouteService
      .routeGpxUpload(
        parameters.initialProperties.groupName,
        parameters.properties.name,
        parameters.referenceFile,
        relationId
      )
      .pipe(
        tap(() => {
          this._state.update((state) => ({
            ...state,
            uploadGpxStatus: 'done',
          }));
        })
      );
  }

  private analyze(parameters: MonitorRouteParameters) {
    this._state.update((state) => ({
      ...state,
      analyzeStatus: 'busy',
    }));

    return this.monitorRouteService
      .routeAnalyze(
        parameters.initialProperties.groupName,
        parameters.properties.name
      )
      .pipe(
        tap(() => {
          this._state.update((state) => ({
            ...state,
            analyzeStatus: 'done',
            done: true,
          }));
        })
      );
  }
}
