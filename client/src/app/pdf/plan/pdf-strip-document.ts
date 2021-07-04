import * as JsPdf from 'jspdf';
import { Plan } from '../../map/planner/plan/plan';
import { PdfPage } from './pdf-page';
import { PdfPlanBuilder } from './pdf-plan-builder';
import { PdfSideBar } from './pdf-side-bar';
import { PdfFooter } from './pdf-footer';
import { PdfPlanNode } from './pdf-plan-node';
import { BitmapIconService } from '../bitmap-icon.service';
import { PdfStripDocumentModel } from './pdf-strip-document-model';

export class PdfStripDocument {
  private readonly doc = new JsPdf();

  private readonly model: PdfStripDocumentModel;

  constructor(
    plan: Plan,
    private name: string,
    private iconService: BitmapIconService
  ) {
    const pdfPlan = PdfPlanBuilder.fromPlan(plan);
    this.model = new PdfStripDocumentModel(pdfPlan.nodes);
  }

  print(): void {
    this.drawPlan();
    this.doc.save('knooppuntnet-strip.pdf');
  }

  private drawPlan() {
    const pageCount = this.model.pageCount();

    for (let pageIndex = 0; pageIndex < pageCount; pageIndex++) {
      if (pageIndex > 0) {
        this.doc.addPage();
      }
      new PdfSideBar(this.doc, this.name).print();
      new PdfFooter(this.doc).print(pageCount, pageIndex);

      this.drawLaneLine(this.model.xContentsLeftWithExtraMargin);

      const pageNodesCount = this.model.nodeCountOnPage(pageIndex);
      const columnCount = this.model.columnCountOnPage(pageIndex);

      for (let columnIndex = 0; columnIndex < columnCount; columnIndex++) {
        const xLeft =
          this.model.xContentsLeftWithExtraMargin +
          this.model.columnWidth * columnIndex;
        const xRight = xLeft + this.model.columnWidth;
        this.drawLaneLine(xRight);

        const rowCount = this.model.calculateRowCount(
          pageNodesCount,
          columnCount,
          columnIndex
        );
        for (let rowIndex = 0; rowIndex < rowCount; rowIndex++) {
          const node = this.model.node(pageIndex, columnIndex, rowIndex);
          const y = PdfPage.yContentsTop + this.model.rowHeight * rowIndex;
          this.drawNode(xLeft, y, node);
        }
      }
    }
  }

  private drawNode(x: number, y: number, node: PdfPlanNode): void {
    const xCircleCenter = x + PdfPage.spacer + this.model.circleRadius;
    const yCircleCenter = y + this.model.circleRadius;

    const xCumulativeDistance =
      xCircleCenter + this.model.circleRadius + PdfPage.spacer;
    const yDistance = yCircleCenter + this.model.circleRadius + PdfPage.spacer;

    this.doc.setLineWidth(0.05);
    this.doc.circle(xCircleCenter, yCircleCenter, this.model.circleRadius, 'S');

    this.doc.setFontSize(12);
    this.doc.text(node.nodeName, xCircleCenter, yCircleCenter, {
      align: 'center',
      baseline: 'middle',
      lineHeightFactor: '1',
    });

    this.doc.setFontSize(8);
    this.doc.text(node.cumulativeDistance, xCumulativeDistance, yCircleCenter, {
      baseline: 'middle',
      lineHeightFactor: '1',
    });
    this.doc.text(node.distance, xCircleCenter, yDistance, {
      align: 'center',
      baseline: 'top',
      lineHeightFactor: '1',
    });
  }

  private drawLaneLine(x: number): void {
    const y = PdfPage.height - PdfPage.marginBottom;
    this.doc.setDrawColor(180);
    this.doc.setLineWidth(0.1);
    this.doc.setLineDash([2, 2]);
    this.doc.line(x, PdfPage.yContentsTop, x, y);
    this.doc.setLineDash([]);

    this.iconService.getIcon('scissors').subscribe((icon) => {
      this.doc.addImage(icon, 'PNG', x - 3, y - 10, 6, 6, '', 'FAST');
    });

    this.doc.setDrawColor(1);
  }
}
