import * as JsPdf from 'jspdf'
import {Plan} from "../planner/plan/plan";
import {PdfPage} from "./pdf-page";
import {PdfPlanBuilder} from "./pdf-plan-builder";
import {PdfSideBar} from "./pdf-side-bar";

export class PdfHorizontal {

  private readonly maxRowsPerPage = 14;
  private readonly maxColumnCount = 7;

  private readonly totalColumnWidth = (PdfPage.xContentsRight - PdfPage.xContentsLeft) / this.maxColumnCount;

  private readonly legDistanceWidth = 5;
  private readonly cumulativeDistanceHeight = 5;
  private readonly totalRowHeight = (PdfPage.yContentsBottom - PdfPage.yContentsTop + PdfPage.spacer) / this.maxRowsPerPage;
  private readonly rowHeight = this.totalRowHeight - PdfPage.spacer;
  private readonly nodeNumberHeight = this.rowHeight - this.cumulativeDistanceHeight;

  private readonly doc = new JsPdf();

  constructor(private plan: Plan) {
  }

  print(): void {
    this.drawGrid();
    this.doc.save("route.pdf");
  }

  private drawGrid() {

    const pdfPlan = PdfPlanBuilder.fromPlan(this.plan);

    const maxNodesPerPage = this.maxRowsPerPage * this.maxColumnCount;
    let pageCount = Math.floor(pdfPlan.nodes.size / maxNodesPerPage);
    if ((pdfPlan.nodes.size % maxNodesPerPage) > 0) {
      pageCount++;
    }

    for (let pageIndex = 0; pageIndex < pageCount; pageIndex++) {
      if (pageIndex > 0) {
        this.doc.addPage();
      }
      new PdfSideBar(this.doc).print();

      let rowCount = 0;
      let pageNodesCount = 0;
      if (pageIndex < pageCount - 1) {
        pageNodesCount = maxNodesPerPage;
        rowCount = this.maxRowsPerPage;
      } else {
        pageNodesCount = pdfPlan.nodes.size % maxNodesPerPage;
        rowCount = Math.floor(pageNodesCount / this.maxColumnCount);
        if ((pageNodesCount % this.maxColumnCount) > 0) {
          rowCount++;
        }
      }

      for (let rowIndex = 0; rowIndex < rowCount; rowIndex++) {

        let columnCount = 0;
        if (rowIndex < rowCount - 1) {
          columnCount = this.maxColumnCount;
        } else {
          columnCount = pageNodesCount % this.maxColumnCount;
          if (columnCount == 0) {
            columnCount = this.maxColumnCount;
          }
        }

        const yTop = PdfPage.marginTop + (rowIndex * this.totalRowHeight);
        const yBottom = yTop + this.rowHeight;

        const xRowRight = PdfPage.xContentsLeft + (columnCount * this.totalColumnWidth);

        this.doc.setDrawColor(180);
        this.doc.setLineWidth(0.1);
        this.doc.line(PdfPage.xContentsLeft, yTop, xRowRight, yTop);
        this.doc.line(PdfPage.xContentsLeft, yBottom, xRowRight, yBottom);

        for (let columnIndex = 0; columnIndex < columnCount; columnIndex++) {

          const nodeIndex = (pageIndex * maxNodesPerPage) + (rowIndex * this.maxColumnCount) + columnIndex;

          if (nodeIndex < pdfPlan.nodes.size) {
            const node = pdfPlan.nodes.get(nodeIndex);

            const xColLeft = PdfPage.xContentsLeft + (columnIndex * this.totalColumnWidth);
            const xColRight = xColLeft + this.totalColumnWidth;
            const xLegDistanceLeft = xColRight - this.legDistanceWidth;

            this.doc.setDrawColor(180);
            this.doc.setLineWidth(0.1);
            this.doc.line(xColLeft, yBottom - this.cumulativeDistanceHeight, xLegDistanceLeft, yBottom - this.cumulativeDistanceHeight);

            if (columnIndex == 0) {
              this.doc.line(xColLeft, yTop, xColLeft, yBottom);
            }
            this.doc.line(xColRight, yTop, xColRight, yBottom);
            this.doc.line(xLegDistanceLeft, yTop, xLegDistanceLeft, yBottom);

            this.doc.setDrawColor(0);
            const xNodeNumberText = xColLeft + ((xLegDistanceLeft - xColLeft) / 2);
            const yNodeNumberText = yTop + (this.nodeNumberHeight / 2);
            this.doc.setFontSize(20);
            this.doc.text(node.nodeName, xNodeNumberText, yNodeNumberText, {align: "center", baseline: "middle", lineHeightFactor: "1"});

            const yCumulativeDistance = yBottom - (this.cumulativeDistanceHeight / 2);
            this.doc.setFontSize(8);
            this.doc.text(node.cumulativeDistance, xNodeNumberText, yCumulativeDistance, {align: "center", baseline: "middle", lineHeightFactor: "1"});

            const xLegDistance = xLegDistanceLeft + (this.legDistanceWidth / 2);
            this.doc.setFontSize(8);
            const widthLegDistanceText = this.doc.getTextWidth("1200 m");
            const yLegDistance = yTop + (this.rowHeight / 2) + (widthLegDistanceText / 2);
            this.doc.text(node.distance, xLegDistance + 1, yLegDistance, {angle: 90, lineHeightFactor: "1"});

          }
        }
      }
    }
  }
}
