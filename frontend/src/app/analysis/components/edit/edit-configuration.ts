import { EditParameters } from './edit-parameters';

export class EditConfiguration {
  readonly nodeChunkSize = 50;
  readonly relationChunkSize = 50;
  readonly requestDelay = 200;
  readonly josmUrl = 'http://localhost:8111/';
  readonly apiUrl = this.josmUrl + 'import?url=https://api.openstreetmap.org/api/0.6';

  seconds(parameters: EditParameters): number {
    let stepCount = 0;

    if (parameters.nodeIds) {
      stepCount += parameters.nodeIds.length / this.nodeChunkSize + 1;
    }

    if (parameters.wayIds) {
      stepCount += parameters.wayIds.length;
    }

    if (parameters.relationIds) {
      stepCount += parameters.relationIds.length / this.relationChunkSize + 1;
    }

    if (parameters.fullRelation === true) {
      stepCount += parameters.relationIds.length;
    }

    return Math.round((stepCount * (this.requestDelay + 200)) / 1000);
  }
}
