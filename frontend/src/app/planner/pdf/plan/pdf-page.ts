export class PdfPage {
  // A4 page size
  static width = 210;
  static height = 297;

  static marginLeft = 10;
  static marginRight = 10;
  static marginTop = 10;
  static marginBottom = 10;
  static footerHeight = 8;

  static sidebarWidth = 7;

  static spacer = 3;

  static xContentsLeft = PdfPage.marginLeft + PdfPage.sidebarWidth + PdfPage.spacer;
  static xContentsRight = PdfPage.width - PdfPage.marginRight;
  static yContentsTop = PdfPage.marginTop;
  static yContentsBottom = PdfPage.height - PdfPage.marginBottom - PdfPage.footerHeight;
}
