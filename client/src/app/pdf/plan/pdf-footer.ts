import * as JsPdf from "jspdf";
import {PdfPage} from "./pdf-page";

export class PdfFooter {

  constructor(private doc: JsPdf) {
  }

  print(pageCount: number, pageIndex: number): void {

    const text = this.today() + "  " + (pageIndex + 1) + "/" + pageCount;

    this.doc.setTextColor(180);
    this.doc.setFontSize(10);
    const textWidth = this.doc.getTextWidth(text);
    const xLeft = PdfPage.xContentsRight - textWidth;
    const yBottom = PdfPage.height - PdfPage.marginBottom;
    this.doc.text(text, xLeft, yBottom);
    this.doc.setTextColor(0);
  }

  private today(): string {
    const date = new Date();
    const dd = String(date.getDate()).padStart(2, "0");
    const mm = String(date.getMonth() + 1).padStart(2, "0"); //January is 0!
    const yyyy = date.getFullYear();
    return `${dd}/${mm}/${yyyy}`;
  }

}
