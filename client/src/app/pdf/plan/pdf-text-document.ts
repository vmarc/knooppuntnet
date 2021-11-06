import { PlannerService } from '@app/map/planner.service';
import { Plan } from '@app/map/planner/plan/plan';
import { PdfPlanBuilder } from '@app/pdf/plan/pdf-plan-builder';
import { PdfPlanNode } from '@app/pdf/plan/pdf-plan-node';
import { jsPDF } from 'jspdf';
import { PdfFooter } from './pdf-footer';
import { PdfPage } from './pdf-page';
import { PdfSideBar } from './pdf-side-bar';
import { PdfTextDocumentModel } from './pdf-text-document-model';

export class PdfTextDocument {
  private readonly doc = new jsPDF();

  private readonly model: PdfTextDocumentModel;

  constructor(
    plan: Plan,
    private name: string,
    private plannerService: PlannerService
  ) {
    const pdfPlan = PdfPlanBuilder.fromPlan(plan);
    this.model = new PdfTextDocumentModel(pdfPlan.nodes);
  }

  print(): void {
    this.drawPlan();
    this.doc.save('knooppuntnet-text.pdf');
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
          const nodeIndex = this.model.nodeIndex(
            pageIndex,
            columnIndex,
            rowIndex
          );
          const node = this.model.node(pageIndex, columnIndex, rowIndex);
          const y = PdfPage.yContentsTop + this.model.rowHeight * rowIndex;
          this.drawNode(xLeft, y, node);
          if (nodeIndex < this.model.nodes.size - 1) {
            // draw line between nodes
            const x1 = xLeft + this.model.xCircleCenter;
            const x2 = x1;
            const y1 = y + this.model.circleRadius + this.model.circleRadius;
            const y2 = y + this.model.rowHeight;
            this.doc.setLineWidth(0.2);
            this.doc.line(x1, y1, x2, y2);
          }
        }
      }
    }
  }

  private drawNode(x: number, y: number, node: PdfPlanNode): void {
    const yNode = y + this.model.circleRadius;
    const circleHeigth = this.model.circleRadius * 2;
    const yRoute = y + circleHeigth + (this.model.rowHeight - circleHeigth) / 2;

    this.doc.setFontSize(8);
    this.doc.text(
      node.cumulativeDistance,
      x + this.model.xCumulativeDistance,
      yNode,
      {
        align: 'left',
        baseline: 'middle',
        lineHeightFactor: 1,
      }
    );

    if (node.flag) {
      this.doc.setLineWidth(0.8);
      this.doc.setFillColor(235, 235, 235);
    } else {
      this.doc.setLineWidth(0.5);
      this.doc.setFillColor(255, 255, 255);
    }

    this.doc.circle(
      x + this.model.xCircleCenter,
      yNode,
      this.model.circleRadius,
      'FD'
    );

    let nodeName = node.nodeName;
    if (node.nodeLongName) {
      nodeName = nodeName + '  -  ' + node.nodeLongName;
    }

    this.doc.text(nodeName, x + this.model.xNodeName, yNode, {
      align: 'left',
      baseline: 'middle',
      lineHeightFactor: 1,
    });

    if (node.distance !== null) {
      let routeInfo = node.distance;
      if (node.colour) {
        const translatedColour = this.plannerService.colour(node.colour);
        routeInfo = routeInfo + '   ' + translatedColour;
      }

      const routeInfoSplitted = this.doc.splitTextToSize(
        routeInfo,
        this.model.routeInfoWidth
      );

      let yRouteInfo = yRoute;
      if (routeInfoSplitted.length > 1) {
        yRouteInfo = yRouteInfo - 1.5;
      }

      this.doc.text(routeInfoSplitted, x + this.model.xRouteInfo, yRouteInfo, {
        align: 'left',
        baseline: 'middle',
        lineHeightFactor: 1,
      });
    }
  }

  private drawLaneLine(x: number): void {
    const y = PdfPage.height - PdfPage.marginBottom;
    this.doc.setDrawColor(180);
    this.doc.setLineWidth(0.1);
    this.doc.setLineDashPattern([2, 2], 0);
    this.doc.line(x, PdfPage.yContentsTop, x, y);
    this.doc.setLineDashPattern([], 0);
    this.doc.setDrawColor(1);
  }
}
