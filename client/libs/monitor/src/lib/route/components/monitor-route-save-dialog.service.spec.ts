import { MonitorRouteSaveResult } from '@api/common/monitor';
import { MonitorRouteProperties } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';
import { of } from 'rxjs';
import { Observable } from 'rxjs';
import { MonitorRouteServiceMock } from '../monitor-route.service.mock';
import { MonitorRouteParameters } from './monitor-route-parameters';
import { MonitorRouteSaveDialogService } from './monitor-route-save-dialog.service';

/* eslint-disable @typescript-eslint/no-unused-vars */
describe('monitor route save dialog', () => {
  it('add route with reference type gpx', () => {
    class Mock extends MonitorRouteServiceMock {
      override routeAdd(
        groupName: string,
        properties: MonitorRouteProperties
      ): Observable<ApiResponse<MonitorRouteSaveResult>> {
        expect(service.state()).toEqual({
          saveRouteEnabled: true,
          saveRouteStatus: 'busy',
          uploadGpxEnabled: true,
          uploadGpxStatus: 'todo',
          analyzeEnabled: true,
          analyzeStatus: 'todo',
          errors: [],
          done: false,
        });

        const response: ApiResponse<MonitorRouteSaveResult> = {
          result: {
            analyzed: false,
            errors: [],
            exception: undefined,
          },
        };

        return of(response);
      }

      override routeGpxUpload(
        groupName: string,
        name: string,
        referenceFile: File,
        relationId: number
      ): Observable<void> {
        expect(service.state()).toEqual({
          saveRouteEnabled: true,
          saveRouteStatus: 'done',
          uploadGpxEnabled: true,
          uploadGpxStatus: 'busy',
          analyzeEnabled: true,
          analyzeStatus: 'todo',
          errors: [],
          done: false,
        });
        return of(void 0);
      }

      override routeAnalyze(
        groupName: string,
        name: string
      ): Observable<ApiResponse<void>> {
        expect(service.state()).toEqual({
          saveRouteEnabled: true,
          saveRouteStatus: 'done',
          uploadGpxEnabled: true,
          uploadGpxStatus: 'done',
          analyzeEnabled: true,
          analyzeStatus: 'busy',
          errors: [],
          done: false,
        });
        const response: ApiResponse<void> = { result: void 0 };
        return of(response);
      }
    }

    const service = new MonitorRouteSaveDialogService(new Mock());

    const parameters: MonitorRouteParameters = {
      mode: 'add',
      initialProperties: {
        groupName: 'group-name',
      },
      properties: {
        referenceType: 'gpx',
        referenceFilename: 'file-name',
      },
      referenceFile: <File>new Blob(),
    };

    service.init(parameters);

    expect(service.state()).toEqual({
      saveRouteEnabled: true,
      saveRouteStatus: 'done',
      uploadGpxEnabled: true,
      uploadGpxStatus: 'done',
      analyzeEnabled: true,
      analyzeStatus: 'done',
      errors: [],
      done: true,
    });
  });

  it('add route with reference type osm', () => {
    class Mock extends MonitorRouteServiceMock {
      override routeAdd(
        groupName: string,
        properties: MonitorRouteProperties
      ): Observable<ApiResponse<MonitorRouteSaveResult>> {
        expect(service.state()).toEqual({
          saveRouteEnabled: true,
          saveRouteStatus: 'busy',
          uploadGpxEnabled: false,
          uploadGpxStatus: 'todo',
          analyzeEnabled: false,
          analyzeStatus: 'todo',
          errors: [],
          done: false,
        });
        const response: ApiResponse<MonitorRouteSaveResult> = {
          result: {
            analyzed: true,
            errors: [],
            exception: undefined,
          },
        };
        return of(response);
      }
    }

    const service = new MonitorRouteSaveDialogService(new Mock());

    const parameters: MonitorRouteParameters = {
      mode: 'add',
      initialProperties: {
        groupName: 'group-name',
      },
      properties: {
        referenceType: 'osm',
      },
      referenceFile: null,
    };

    service.init(parameters);

    expect(service.state()).toEqual({
      saveRouteEnabled: true,
      saveRouteStatus: 'done',
      uploadGpxEnabled: false,
      uploadGpxStatus: 'todo',
      analyzeEnabled: false,
      analyzeStatus: 'todo',
      errors: [],
      done: true,
    });
  });

  it('add route with reference type multi-gpx', () => {
    class Mock extends MonitorRouteServiceMock {
      override routeAdd(
        groupName: string,
        properties: MonitorRouteProperties
      ): Observable<ApiResponse<MonitorRouteSaveResult>> {
        expect(service.state()).toEqual({
          saveRouteEnabled: true,
          saveRouteStatus: 'busy',
          uploadGpxEnabled: false,
          uploadGpxStatus: 'todo',
          analyzeEnabled: false,
          analyzeStatus: 'todo',
          errors: [],
          done: false,
        });
        const response: ApiResponse<MonitorRouteSaveResult> = {
          result: {
            analyzed: false,
            errors: [],
            exception: undefined,
          },
        };
        return of(response);
      }
    }

    const service = new MonitorRouteSaveDialogService(new Mock());

    const parameters: MonitorRouteParameters = {
      mode: 'add',
      initialProperties: {
        groupName: 'group-name',
      },
      properties: {
        referenceType: 'multi-gpx',
      },
      referenceFile: null,
    };

    service.init(parameters);

    expect(service.state()).toEqual({
      saveRouteEnabled: true,
      saveRouteStatus: 'done',
      uploadGpxEnabled: false,
      uploadGpxStatus: 'todo',
      analyzeEnabled: false,
      analyzeStatus: 'todo',
      errors: [],
      done: true,
    });
  });
});
