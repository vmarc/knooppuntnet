import {PdfPage} from "./pdf-page";
import {PdfPlanNode} from "./pdf-plan-node";
import {List} from "immutable";

export class PdfStripDocumentModel {

  private readonly textHeight = 8;

  readonly circleRadius = 5;
  readonly columnWidth = PdfPage.spacer + this.circleRadius + PdfPage.spacer + 20;
  readonly rowHeight = this.circleRadius + PdfPage.spacer + PdfPage.spacer + this.textHeight;

  readonly xContentsLeftWithExtraMargin = PdfPage.xContentsLeft + 14;

  private readonly maxRowCount = Math.floor((PdfPage.yContentsBottom - PdfPage.yContentsTop) / this.rowHeight);
  private readonly maxColumnCount = Math.floor((PdfPage.xContentsRight - this.xContentsLeftWithExtraMargin) / this.columnWidth);
  private readonly maxNodesPerPage = this.maxRowCount * this.maxColumnCount;

  constructor(private nodes: List<PdfPlanNode>) {
  }

  pageCount(): number {
    let pageCount = Math.floor(this.nodes.size / this.maxNodesPerPage);
    if ((this.nodes.size % this.maxNodesPerPage) > 0) {
      pageCount++;
    }
    return pageCount;
  }

  nodeCountOnPage(pageIndex: number): number {
    let pageNodesCount = 0;
    if (pageIndex < this.pageCount() - 1) {
      pageNodesCount = this.maxNodesPerPage;
    } else {
      const remainder = this.nodes.size % this.maxNodesPerPage;
      if (remainder === 0) {
        pageNodesCount = this.maxNodesPerPage;
      } else {
        pageNodesCount = remainder;
      }
    }
    return pageNodesCount;
  }

  columnCountOnPage(pageIndex: number): number {
    let columnCount = 0;
    if (pageIndex < this.pageCount() - 1) {
      columnCount = this.maxColumnCount;
    } else {
      const pageNodesCount = this.nodeCountOnPage(pageIndex);
      columnCount = Math.floor(pageNodesCount / this.maxRowCount);
      if ((pageNodesCount % this.maxRowCount) > 0) {
        columnCount++;
      }
    }
    return columnCount;
  }

  calculateRowCount(pageNodesCount: number, columnCount: number, columnIndex: number): number {
    let rowCount = 0;
    if (columnIndex < columnCount - 1) {
      rowCount = this.maxRowCount;
    } else {
      rowCount = pageNodesCount % this.maxRowCount;
      if (rowCount === 0) {
        rowCount = this.maxRowCount;
      }
    }
    return rowCount;
  }

  node(pageIndex: number, columnIndex: number, rowIndex: number): PdfPlanNode {
    const nodeIndex = (pageIndex * this.maxNodesPerPage) + (columnIndex * this.maxRowCount) + rowIndex;
    return this.nodes.get(nodeIndex);
  }

}
