import { jsPDF } from 'jspdf';
import { PdfPage } from './pdf-page';

export class PdfSideBar {
  constructor(private doc: jsPDF, private name: string) {}

  print(): void {
    const xLeft = PdfPage.marginLeft;
    const xRight = PdfPage.marginLeft + PdfPage.sidebarWidth;
    const yTop = PdfPage.marginTop;
    const yBottom = PdfPage.height - PdfPage.marginBottom;

    this.doc.setDrawColor(180);
    this.doc.setTextColor(180);
    this.doc.setLineWidth(0.1);

    this.doc.line(xLeft, yTop, xRight, yTop);
    this.doc.line(xLeft, yBottom, xRight, yBottom);
    this.doc.line(xLeft, yTop, xLeft, yBottom);
    this.doc.line(xRight, yTop, xRight, yBottom);

    this.doc.setFontSize(12);
    const textWidth = this.doc.getTextWidth(this.name);
    this.doc.text(this.name, xLeft + 5, yTop + 5 + textWidth, { angle: 90 });
    this.doc.text('knooppuntnet', xLeft + 5, yBottom - 5, { angle: 90 });

    this.doc.setDrawColor(0);
    this.doc.setTextColor(0);
  }
}
