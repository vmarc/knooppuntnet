export class PlanInstruction {
  constructor(readonly node: string,
              readonly command: string,
              readonly heading: string,
              readonly street: string,
              readonly distance: number,
              readonly colour: string) {
  }
}
