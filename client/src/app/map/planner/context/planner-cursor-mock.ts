export class PlannerCursorMock {

  style = "default";

  setStyle(style: string): void {
    this.style = style;
  }

  expectStyle(style: string): void {
    expect(this.style).toEqual(style);
  }
}
