import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { ApiService } from '@app/shared/services/api.service';
import { Range } from 'immutable';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { delay } from 'rxjs/operators';
import { EditConfiguration } from './edit-configuration';
import { EditParameters } from './edit-parameters';

@Injectable()
export class EditStepBuilder {
  private readonly apiService = inject(ApiService);
  private readonly configuration = new EditConfiguration();

  build(parameters: EditParameters, updateProgress: () => void): Observable<string>[] {
    const nodeEdits = this.buildNodeEdits(parameters, updateProgress);
    const wayEdits = this.buildWayEdits(parameters, updateProgress);
    const relationEdits = this.buildRelationEdits(parameters, updateProgress);
    const fullRelationEdits = this.buildFullRelationEdits(parameters, updateProgress);
    const edits = nodeEdits.concat(wayEdits).concat(relationEdits).concat(fullRelationEdits);
    const setBounds = this.buildSetBounds(parameters, updateProgress);
    return setBounds === null ? edits : edits.concat(setBounds);
  }

  private buildSetBounds(
    parameters: EditParameters,
    updateProgress: () => void
  ): Observable<string> {
    if (parameters.bounds) {
      const zoomUrl =
        this.configuration.josmUrl +
        `zoom?left=${parameters.bounds.minLon}&right=${parameters.bounds.maxLon}&top=${parameters.bounds.maxLat}&bottom=${parameters.bounds.minLat}`;
      return this.apiService.edit(zoomUrl).pipe(tap(updateProgress));
    }
    return null;
  }

  private buildNodeEdits(
    parameters: EditParameters,
    updateProgress: () => void
  ): Observable<string>[] {
    if (!parameters.nodeIds || parameters.nodeIds.length === 0) {
      return [];
    }
    const nodeBatches = Range(0, parameters.nodeIds.length, this.configuration.nodeChunkSize)
      .map((chunkStart) =>
        parameters.nodeIds.slice(chunkStart, chunkStart + this.configuration.nodeChunkSize)
      )
      .toArray();
    return nodeBatches.map((nodeIds) => {
      const nodeIdString = nodeIds.join(',');
      const url = `${this.configuration.apiUrl}/nodes?nodes=${nodeIdString}`;
      return this.apiService
        .edit(url)
        .pipe(tap(updateProgress), delay(this.configuration.requestDelay));
    });
  }

  private buildWayEdits(
    parameters: EditParameters,
    updateProgress: () => void
  ): Observable<string>[] {
    if (!parameters.wayIds || parameters.wayIds.length === 0) {
      return [];
    }

    return parameters.wayIds.map((wayId) => {
      const url = `${this.configuration.apiUrl}/way/${wayId}/full`;
      return this.apiService
        .edit(url)
        .pipe(tap(updateProgress), delay(this.configuration.requestDelay));
    });
  }

  private buildRelationEdits(
    parameters: EditParameters,
    updateProgress: () => void
  ): Observable<string>[] {
    if (!parameters.relationIds || parameters.relationIds.length === 0) {
      return [];
    }
    const relationBatches = Range(
      0,
      parameters.relationIds.length,
      this.configuration.relationChunkSize
    )
      .map((chunkStart) =>
        parameters.relationIds.slice(chunkStart, chunkStart + this.configuration.relationChunkSize)
      )
      .toArray();
    return relationBatches.map((relationIds) => {
      const relationIdString = relationIds.join(',');
      const url = `${this.configuration.apiUrl}/relations?relations=${relationIdString}`;
      return this.apiService
        .edit(url)
        .pipe(tap(updateProgress), delay(this.configuration.requestDelay));
    });
  }

  private buildFullRelationEdits(
    parameters: EditParameters,
    updateProgress: () => void
  ): Observable<string>[] {
    if (!parameters.fullRelation) {
      return [];
    }
    return parameters.relationIds.map((relationId) => {
      const url = `${this.configuration.apiUrl}/relation/${relationId}/full`;
      return this.apiService
        .edit(url)
        .pipe(tap(updateProgress), delay(this.configuration.requestDelay));
    });
  }
}
