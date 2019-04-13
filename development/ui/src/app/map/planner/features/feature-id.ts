export class FeatureId {

  static id: number = 0;

  static next(): string {
    return "" + ++this.id;
  }

}
