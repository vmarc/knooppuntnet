import {PdfPage} from "./pdf-page";
import {PdfPlanNode} from "./pdf-plan-node";
import {List} from "immutable";

export class PdfDocumentModel {

  private readonly maxRowsPerPage = 14;
  private readonly maxColumnCount = 7;

  readonly totalColumnWidth = (PdfPage.xContentsRight - PdfPage.xContentsLeft) / this.maxColumnCount;

  readonly legDistanceWidth = 5;
  readonly cumulativeDistanceHeight = 5;
  readonly totalRowHeight = (PdfPage.yContentsBottom - PdfPage.yContentsTop + PdfPage.spacer) / this.maxRowsPerPage;
  readonly rowHeight = this.totalRowHeight - PdfPage.spacer;
  readonly nodeNumberHeight = this.rowHeight - this.cumulativeDistanceHeight;

  constructor(private nodes: List<PdfPlanNode>) {
  }

  pageCount(): number {
    const maxNodesPerPage = this.maxRowsPerPage * this.maxColumnCount;
    let pageCount = Math.floor(this.nodes.size / maxNodesPerPage);
    if ((this.nodes.size % maxNodesPerPage) > 0) {
      pageCount++;
    }
    return pageCount;
  }

  pageNodeCount(pageIndex: number): number {
    const maxNodesPerPage = this.maxRowsPerPage * this.maxColumnCount;
    let pageNodesCount = 0;
    if (pageIndex < this.pageCount() - 1) {
      pageNodesCount = maxNodesPerPage;
    } else {
      const remainder = this.nodes.size % maxNodesPerPage;
      if (remainder === 0) {
        pageNodesCount = maxNodesPerPage;
      } else {
        pageNodesCount = remainder;
      }
    }
    return pageNodesCount;
  }

  pageRowCount(pageIndex: number): number {
    const maxNodesPerPage = this.maxRowsPerPage * this.maxColumnCount;
    let rowCount = 0;
    if (pageIndex < this.pageCount() - 1) {
      rowCount = this.maxRowsPerPage;
    } else {
      const pageNodeCount = this.pageNodeCount(pageIndex);
      rowCount = Math.floor(pageNodeCount / this.maxColumnCount);
      if ((pageNodeCount % this.maxColumnCount) > 0) {
        rowCount++;
      }
    }
    return rowCount;
  }

  columnCount(pageIndex: number, rowCount: number, rowIndex: number): number {
    const pageNodesCount = this.pageNodeCount(pageIndex);
    let columnCount = 0;
    if (rowIndex < rowCount - 1) {
      columnCount = this.maxColumnCount;
    } else {
      columnCount = pageNodesCount % this.maxColumnCount;
      if (columnCount == 0) {
        columnCount = this.maxColumnCount;
      }
    }
    return columnCount;
  }

  node(pageIndex: number, rowIndex: number, columnIndex: number): PdfPlanNode {
    const maxNodesPerPage = this.maxRowsPerPage * this.maxColumnCount;
    const nodeIndex = (pageIndex * maxNodesPerPage) + (rowIndex * this.maxColumnCount) + columnIndex;
    return this.nodes.get(nodeIndex);
  }

}
