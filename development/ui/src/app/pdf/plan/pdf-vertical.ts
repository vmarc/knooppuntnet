import * as JsPdf from 'jspdf'
import {Plan} from "../../map/planner/plan/plan";
import {PdfPage} from "./pdf-page";
import {PdfPlanBuilder} from "./pdf-plan-builder";
import {PdfSideBar} from "./pdf-side-bar";

export class PdfVertical {

  private readonly doc = new JsPdf();

  constructor(private plan: Plan) {
  }

  print(): void {
    this.drawPlan();
    this.doc.save("knooppuntnet.pdf");
  }

  private drawPlan() {

    new PdfSideBar(this.doc).print();

    const pdfPlan = PdfPlanBuilder.fromPlan(this.plan);

    const circleRadius = 5;
    const columnWidth = PdfPage.spacer + circleRadius + PdfPage.spacer + 20;

    const xLeft = 30;
    const xRight = xLeft + columnWidth;
    const yTop = PdfPage.marginTop;
    const yBottom = PdfPage.height - PdfPage.marginBottom;

    this.doc.setFontSize(10);

    this.doc.setDrawColor(180);
    this.doc.setLineWidth(0.1);
    this.doc.setLineDash([2, 2]);
    this.doc.line(xLeft, yTop, xLeft, yBottom);
    this.doc.setLineWidth(0.05);
    this.doc.line(xRight, yTop, xRight, yBottom);
    this.doc.setLineDash([]);

    this.doc.setDrawColor(1);

    const nodes = pdfPlan.nodes;

    const textHeight = 8;
    const columnCount = 1;
    const rowCount = nodes.size;
    const rowHeight = circleRadius + PdfPage.spacer + PdfPage.spacer + textHeight;
    const rowsPerColumn = 14;


    for (let columnIndex = 0; columnIndex < columnCount; columnIndex++) {
      for (let rowIndex = 0; rowIndex < rowCount; rowIndex++) {
        const nodeIndex = (columnIndex * rowsPerColumn) + rowIndex;
        if (nodeIndex < nodes.size) {
          const node = nodes.get(nodeIndex);

          const y = PdfPage.marginTop + (rowIndex * rowHeight);
          const xCircleCenter = xLeft + PdfPage.spacer + circleRadius;
          const yCircleCenter = y + circleRadius;

          const xCumulativeDistance = xCircleCenter + circleRadius + PdfPage.spacer;
          const yDistance = yCircleCenter + circleRadius + PdfPage.spacer;

          this.doc.setLineWidth(0.05);
          this.doc.circle(xCircleCenter, yCircleCenter, circleRadius);

          this.doc.setFontSize(12);
          this.doc.text(node.nodeName, xCircleCenter, yCircleCenter, {align: "center", baseline: "middle", lineHeightFactor: "1"});

          this.doc.setFontSize(8);
          this.doc.text(node.cumulativeDistance, xCumulativeDistance, yCircleCenter, {baseline: "middle", lineHeightFactor: "1"});
          this.doc.text(node.distance, xCircleCenter, yDistance, {align: "center", baseline: "top", lineHeightFactor: "1"});
        }
      }
    }
  }
}
