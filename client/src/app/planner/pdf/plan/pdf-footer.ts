import { Util } from '@app/components/shared';
import { jsPDF } from 'jspdf';
import { PdfPage } from './pdf-page';

export class PdfFooter {
  constructor(private doc: jsPDF) {}

  print(pageCount: number, pageIndex: number): void {
    const text = this.today() + '  ' + (pageIndex + 1) + '/' + pageCount;

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
    const dd = Util.twoDigits(date.getDate());
    const mm = Util.twoDigits(date.getMonth() + 1); // January is 0!
    const yyyy = date.getFullYear();
    return `${yyyy}-${mm}-${dd}`;
  }
}
