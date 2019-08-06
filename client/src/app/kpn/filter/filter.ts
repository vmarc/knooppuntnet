export abstract class Filter<T> {

  constructor(readonly name: string) {
  }

  abstract passes(element: T): boolean;
}
