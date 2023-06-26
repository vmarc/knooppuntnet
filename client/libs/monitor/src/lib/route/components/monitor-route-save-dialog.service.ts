import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { mergeMap } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { MonitorRouteService } from '../monitor-route.service';
import { MonitorRouteParameters } from './monitor-route-parameters';
import { initialState } from './monitor-route-save-dialog.state';
import { MonitorRouteSaveDialogState } from './monitor-route-save-dialog.state';

@Injectable()
export class MonitorRouteSaveDialogService {
  readonly #monitorRouteService = inject(MonitorRouteService);

  readonly #state = signal<MonitorRouteSaveDialogState>(initialState);
  readonly state = this.#state.asReadonly();

  init(parameters: MonitorRouteParameters): void {
    if (parameters.mode === 'add') {
      this.#initAdd(parameters);
    } else {
      this.#initUpdate(parameters);
    }
  }

  #initAdd(parameters: MonitorRouteParameters): void {
    const gpx = parameters.properties.referenceType === 'gpx';
    this.#state.update((state) => ({
      ...state,
      saveRouteEnabled: true,
      saveRouteStatus: 'busy',
      uploadGpxEnabled: gpx,
      analyzeEnabled: gpx,
    }));

    if (gpx) {
      this.#saveRoute(parameters)
        .pipe(
          mergeMap(() => this.#upload(parameters)),
          mergeMap(() => this.#analyze(parameters))
        )
        .subscribe();
    } else {
      this.#saveRoute(parameters).subscribe();
    }
  }

  #initUpdate(parameters: MonitorRouteParameters): void {
    const properties = parameters.properties;
    const gpx =
      properties.referenceType === 'gpx' && properties.referenceFileChanged;

    this.#state.update((state) => {
      return {
        ...state,
        saveRouteEnabled: true,
        saveRouteStatus: 'busy',
        uploadGpxEnabled: gpx,
        analyzeEnabled: gpx,
      };
    });

    if (gpx) {
      this.#updateRoute(parameters)
        .pipe(
          mergeMap(() => this.#upload(parameters)),
          mergeMap(() => this.#analyze(parameters))
        )
        .subscribe();
    } else {
      this.#updateRoute(parameters).subscribe();
    }
  }

  #saveRoute(parameters: MonitorRouteParameters) {
    const groupName = parameters.initialProperties.groupName;
    const properties = parameters.properties;
    this.#state.update((state) => ({
      ...state,
      saveRouteStatus: 'busy',
    }));

    return this.#monitorRouteService.routeAdd(groupName, properties).pipe(
      tap((response) => {
        const done = !(
          this.state().uploadGpxEnabled || this.state().analyzeEnabled
        );
        this.#state.update((state) => ({
          ...state,
          saveRouteStatus: 'done',
          errors: response.result?.errors,
          exception: response.result?.exception,
          done,
        }));
      })
    );
  }

  #updateRoute(parameters: MonitorRouteParameters) {
    const properties = parameters.properties;
    this.#state.update((state) => ({
      ...state,
      saveRouteStatus: 'busy',
    }));

    return this.#monitorRouteService
      .routeUpdate(
        parameters.initialProperties.groupName,
        parameters.initialProperties.name,
        properties
      )
      .pipe(
        tap((response) => {
          const done = !(
            this.state().uploadGpxEnabled || this.state().analyzeEnabled
          );
          this.#state.update((state) => ({
            ...state,
            saveRouteStatus: 'done',
            errors: response.result?.errors,
            exception: response.result?.exception,
            done,
          }));
        })
      );
  }

  #upload(parameters: MonitorRouteParameters) {
    this.#state.update((state) => ({
      ...state,
      uploadGpxStatus: 'busy',
    }));
    return this.#monitorRouteService
      .routeGpxUpload(
        parameters.initialProperties.groupName,
        parameters.properties.name,
        parameters.referenceFile
      )
      .pipe(
        tap(() => {
          this.#state.update((state) => ({
            ...state,
            uploadGpxStatus: 'done',
          }));
        })
      );
  }

  #analyze(parameters: MonitorRouteParameters) {
    this.#state.update((state) => ({
      ...state,
      analyzeStatus: 'busy',
    }));

    return this.#monitorRouteService
      .routeAnalyze(
        parameters.initialProperties.groupName,
        parameters.properties.name
      )
      .pipe(
        tap(() => {
          this.#state.update((state) => ({
            ...state,
            analyzeStatus: 'done',
            done: true,
          }));
        })
      );
  }
}
