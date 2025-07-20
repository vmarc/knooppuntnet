import { signal } from '@angular/core';

export class SplitState {
  private readonly _left = signal<number>(0);
  private readonly _right = signal<number>(0);

  readonly left = this._left.asReadonly();
  readonly right = this._right.asReadonly();

  update(leftValue: number, rightValue: number): void {
    this._left.set(leftValue);
    this._right.set(rightValue);
  }
}
