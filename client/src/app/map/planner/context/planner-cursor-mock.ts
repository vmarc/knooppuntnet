export class PlannerCursorMock {

  style = "default";

  setStyleMove(): void {
    this.setStyle("move");
  }

  setStylePointer(): void {
    this.setStyle("pointer");
  }

  setStyleDefault(): void {
    this.setStyle("default");
  }

  private setStyle(style: string): void {
    this.style = style;
  }

  expectStyle(style: string): void {
    expect(this.style).toEqual(style);
  }
}
