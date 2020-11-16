export class FeatureId {

  static id = 0;

  static next(): string {
    return '' + ++this.id;
  }

}
