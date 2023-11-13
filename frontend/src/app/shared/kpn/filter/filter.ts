export abstract class Filter<T> {
  protected constructor(readonly name: string) {}

  abstract passes(element: T): boolean;
}
