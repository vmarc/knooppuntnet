export class PdfPlanNode {
  constructor(
    readonly nodeName: string,
    readonly distance: string,
    readonly cumulativeDistance: string,
    readonly colour: string,
    readonly flag: boolean
  ) {}
}
