import * as JsPdf from "jspdf";
import * as QRious from "qrious";
import {Plan} from "../../map/planner/plan/plan";
import {PdfPage} from "./pdf-page";
import {PdfPlanBuilder} from "./pdf-plan-builder";
import {PdfSideBar} from "./pdf-side-bar";
import {PdfFooter} from "./pdf-footer";
import {PdfDocumentModel} from "./pdf-document-model";

export class PdfDocument {

  private readonly model: PdfDocumentModel;

  private readonly doc = new JsPdf();

  constructor(plan: Plan, private planUrl: string) {
    const pdfPlan = PdfPlanBuilder.fromPlan(plan);
    this.model = new PdfDocumentModel(pdfPlan.nodes);
  }

  print(): void {
    this.drawGrid();
    this.drawQrCode();
    this.doc.save("route.pdf");
  }

  private drawQrCode(): void {

    const qrCodeSize = 40;
    const x = PdfPage.xContentsRight - qrCodeSize;
    const y = PdfPage.yContentsBottom - qrCodeSize;

    const qrious = new QRious({
      value: this.planUrl,
      level: "L", // Error correction level of the QR code (L, M, Q, H)
      mime: "image/png",
      size: 200,
      padding: 0
    });

    this.doc.addImage(qrious.toDataURL(), "PNG", x, y, qrCodeSize, qrCodeSize, "", "FAST");
  }

  private drawGrid() {

    const pageCount = this.model.pageCount();

    for (let pageIndex = 0; pageIndex < pageCount; pageIndex++) {
      if (pageIndex > 0) {
        this.doc.addPage();
      }
      new PdfSideBar(this.doc).print();
      new PdfFooter(this.doc).print(pageCount, pageIndex);

      const rowCount = this.model.pageRowCount(pageIndex);

      for (let rowIndex = 0; rowIndex < rowCount; rowIndex++) {

        const columnCount = this.model.columnCount(pageIndex, rowCount, rowIndex);

        const yTop = PdfPage.marginTop + (rowIndex * this.model.totalRowHeight);
        const yBottom = yTop + this.model.rowHeight;

        const xRowRight = PdfPage.xContentsLeft + (columnCount * this.model.totalColumnWidth);

        this.doc.setDrawColor(180);
        this.doc.setLineWidth(0.1);
        this.doc.line(PdfPage.xContentsLeft, yTop, xRowRight, yTop);
        this.doc.line(PdfPage.xContentsLeft, yBottom, xRowRight, yBottom);

        for (let columnIndex = 0; columnIndex < columnCount; columnIndex++) {

          const node = this.model.node(pageIndex, rowIndex, columnIndex);

          const xColLeft = PdfPage.xContentsLeft + (columnIndex * this.model.totalColumnWidth);
          const xColRight = xColLeft + this.model.totalColumnWidth;
          const xLegDistanceLeft = xColRight - this.model.legDistanceWidth;

          this.doc.setDrawColor(180);
          this.doc.setLineWidth(0.1);
          this.doc.line(
            xColLeft,
            yBottom - this.model.cumulativeDistanceHeight,
            xLegDistanceLeft,
            yBottom - this.model.cumulativeDistanceHeight
          );

          if (columnIndex === 0) {
            this.doc.line(xColLeft, yTop, xColLeft, yBottom);
          }
          this.doc.line(xColRight, yTop, xColRight, yBottom);
          this.doc.line(xLegDistanceLeft, yTop, xLegDistanceLeft, yBottom);

          this.doc.setDrawColor(0);
          const xNodeNumberText = xColLeft + ((xLegDistanceLeft - xColLeft) / 2);
          const yNodeNumberText = yTop + (this.model.nodeNumberHeight / 2);
          this.doc.setFontSize(20);
          this.doc.text(
            node.nodeName,
            xNodeNumberText,
            yNodeNumberText,
            {align: "center", baseline: "middle", lineHeightFactor: "1"}
          );

          const yCumulativeDistance = yBottom - (this.model.cumulativeDistanceHeight / 2);
          this.doc.setFontSize(8);
          this.doc.text(
            node.cumulativeDistance,
            xNodeNumberText,
            yCumulativeDistance,
            {align: "center", baseline: "middle", lineHeightFactor: "1"}
          );

          const xLegDistance = xLegDistanceLeft + (this.model.legDistanceWidth / 2);
          this.doc.setFontSize(8);
          const widthLegDistanceText = this.doc.getTextWidth("1200 m");
          const yLegDistance = yTop + (this.model.rowHeight / 2) + (widthLegDistanceText / 2);
          this.doc.text(node.distance, xLegDistance + 1, yLegDistance, {angle: 90, lineHeightFactor: "1"});
        }
      }
    }
  }
}
