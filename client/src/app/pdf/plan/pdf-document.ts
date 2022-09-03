import { Plan } from '@app/map/planner/plan/plan';
import { jsPDF } from 'jspdf';
import { PdfColourBox } from './pdf-colour-box';
import { PdfDocumentModel } from './pdf-document-model';
import { PdfFooter } from './pdf-footer';
import { PdfPage } from './pdf-page';
import { PdfPlanBuilder } from './pdf-plan-builder';
import { PdfSideBar } from './pdf-side-bar';

export class PdfDocument {
  private readonly model: PdfDocumentModel;

  private readonly doc = new jsPDF();

  constructor(
    plan: Plan,
    private planUrl: string,
    private name: string,
    private qrCode: any
  ) {
    const pdfPlan = PdfPlanBuilder.fromPlan(plan);
    this.model = new PdfDocumentModel(pdfPlan.nodes);
  }

  print(): void {
    this.drawGrid();
    this.drawQrCode();
    const filename = this.name.replace(/ /g, '_') + '.pdf';
    this.doc.save(filename);
  }

  private drawQrCode(): void {
    const qrCodeSize = 40;
    const x = PdfPage.xContentsRight - qrCodeSize;
    const y = PdfPage.yContentsBottom - qrCodeSize;
    this.doc.addImage(
      this.qrCode,
      'PNG',
      x,
      y,
      qrCodeSize,
      qrCodeSize,
      '',
      'FAST'
    );
  }

  private drawGrid() {
    const pageCount = this.model.pageCount();

    for (let pageIndex = 0; pageIndex < pageCount; pageIndex++) {
      if (pageIndex > 0) {
        this.doc.addPage();
      }
      new PdfSideBar(this.doc, this.name).print();
      new PdfFooter(this.doc).print(pageCount, pageIndex);

      const rowCount = this.model.pageRowCount(pageIndex);

      for (let rowIndex = 0; rowIndex < rowCount; rowIndex++) {
        const columnCount = this.model.columnCount(
          pageIndex,
          rowCount,
          rowIndex
        );

        const yTop = PdfPage.marginTop + rowIndex * this.model.totalRowHeight;
        const yBottom = yTop + this.model.rowHeight;

        const xRowRight =
          PdfPage.xContentsLeft + columnCount * this.model.totalColumnWidth;

        this.doc.setDrawColor(180);
        this.doc.setLineWidth(0.1);
        this.doc.line(PdfPage.xContentsLeft, yTop, xRowRight, yTop);
        this.doc.line(PdfPage.xContentsLeft, yBottom, xRowRight, yBottom);

        for (let columnIndex = 0; columnIndex < columnCount; columnIndex++) {
          const node = this.model.node(pageIndex, rowIndex, columnIndex);

          const xColLeft =
            PdfPage.xContentsLeft + columnIndex * this.model.totalColumnWidth;
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
          const xNodeNumberText = xColLeft + (xLegDistanceLeft - xColLeft) / 2;
          const yNodeNumberText = yTop + this.model.nodeNumberHeight / 2;

          let nodeName = node.nodeName;
          if (nodeName.length > 8) {
            nodeName = nodeName.slice(0, 8);
          }

          if (nodeName.length <= 3) {
            this.doc.setFontSize(20);
          } else if (nodeName.length <= 6) {
            this.doc.setFontSize(12);
          } else {
            this.doc.setFontSize(10);
          }

          this.doc.text(nodeName, xNodeNumberText, yNodeNumberText, {
            align: 'center',
            baseline: 'middle',
            lineHeightFactor: 1,
          });

          const yCumulativeDistance =
            yBottom - this.model.cumulativeDistanceHeight / 2;

          this.doc.setFontSize(8);
          this.doc.text(
            node.cumulativeDistance,
            xNodeNumberText,
            yCumulativeDistance,
            { align: 'center', baseline: 'middle', lineHeightFactor: 1 }
          );

          if (node.distance !== null) {
            if (node.colour) {
              new PdfColourBox(
                this.doc,
                xLegDistanceLeft + 0.3,
                yBottom - this.model.legDistanceWidth + 0.3,
                this.model.legDistanceWidth - 0.6,
                node.colour
              ).print();
            }

            const xLegDistance =
              xLegDistanceLeft + this.model.legDistanceWidth / 2;
            this.doc.setFontSize(8);
            const widthLegDistanceText = this.doc.getTextWidth(node.distance);
            let yLegDistance =
              yTop + this.model.rowHeight / 2 + widthLegDistanceText / 2;
            if (node.colour) {
              yLegDistance =
                yTop +
                (this.model.rowHeight - this.model.legDistanceWidth) / 2 +
                widthLegDistanceText / 2;
            }

            this.doc.text(node.distance, xLegDistance + 1, yLegDistance, {
              angle: 90,
              lineHeightFactor: 1,
            });
          }
        }
      }
    }
  }
}
