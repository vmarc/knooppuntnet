import {PlannerCursor} from './planner-cursor';

export class PlannerCursorMock implements PlannerCursor {

  style = 'default';

  setStyleGrab(): void {
    this.style = 'grab';
  }

  setStyleGrabbing(): void {
    this.style = 'grabbing';
  }

  setStylePointer(): void {
    this.style = 'pointer';
  }

  setStyleDefault(): void {
    this.style = 'default';
  }

  setStyleWait(): void {
    this.style = 'wait';
  }

  expectStyle(style: string): void {
    expect(this.style).toEqual(style);
  }
}
