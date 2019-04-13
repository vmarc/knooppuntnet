import {MatIconRegistry} from "@angular/material";
import * as canvg from "canvg";
import * as JsPdf from "jspdf";
import {Directions} from "../../kpn/shared/directions/directions";
import {PdfPage} from "./pdf-page";
import {PdfSideBar} from "./pdf-side-bar";

export class PdfDirections {

  private readonly instructionsPerPage = 19;
  private readonly instructionHeight = (PdfPage.yContentsBottom - PdfPage.yContentsTop) / this.instructionsPerPage;

  private readonly doc = new JsPdf();

  constructor(private directions: Directions,
              private iconRegistry: MatIconRegistry) {
  }

  print(): void {
    this.draw();
    this.doc.save("knooppuntnet.pdf");
  }

  private draw() {

    const instructions = this.directions.instructions;

    let pageCount = Math.floor(instructions.size / this.instructionsPerPage);
    if ((instructions.size % this.instructionsPerPage) > 0) {
      pageCount++;
    }

    for (let pageIndex = 0; pageIndex < pageCount; pageIndex++) {
      if (pageIndex > 0) {
        this.doc.addPage();
      }
      new PdfSideBar(this.doc).print();

      let rowCount = 0;
      if (pageIndex < pageCount - 1) {
        rowCount = this.instructionsPerPage;
      } else {
        rowCount = instructions.size % this.instructionsPerPage;
      }


      let x = 30;
      const circleRadius = 5;


      for (let rowIndex = 0; rowIndex < rowCount; rowIndex++) {

        const instructionIndex = (this.instructionsPerPage * pageIndex) + rowIndex;

        if (instructionIndex < instructions.size) {

          const instruction = instructions.get(instructionIndex);

          const y = PdfPage.yContentsTop + (this.instructionHeight * rowIndex);

          this.doc.setDrawColor(230);
          this.doc.setLineWidth(0.05);
          this.doc.line(x, y, PdfPage.width - PdfPage.marginRight, y);

          if (instruction.node) {

            const xCircleCenter = x + circleRadius;
            const yCircleCenter = y + 2.5 + circleRadius;

            this.doc.setLineWidth(0.05);
            this.doc.circle(xCircleCenter, yCircleCenter, circleRadius);
            this.doc.setFontSize(12);
            this.doc.text(instruction.node, xCircleCenter, yCircleCenter, {align: "center", baseline: "middle", lineHeightFactor: "1"});

          } else {
            let yy = y + PdfPage.spacer;
            this.iconRegistry.getNamedSvgIcon(instruction.sign).subscribe(svgElement => {
              const str: string = svgElement.outerHTML;
              const canvas: HTMLCanvasElement = document.createElement("canvas");
              // @ts-ignore
              canvg(canvas, str);
              const imgData = canvas.toDataURL("image/png");
              this.doc.addImage(imgData, "PNG", x, yy, 80, 80);
            });

            const xx = x + (circleRadius * 2) + PdfPage.spacer;
            this.doc.setFontSize(10);
            this.doc.text(instruction.text, xx, yy, {baseline: "top", lineHeightFactor: "1"});
            yy += 5;

            if (instruction.streetName) {
              this.doc.text(instruction.streetName, xx, yy, {baseline: "top", lineHeightFactor: "1"});
              yy += 5;
            }

            this.doc.text(instruction.distance + " m", xx, yy, {baseline: "top", lineHeightFactor: "1"});
            yy += 5;

            // instruction.annotationText
            // instruction.annotationImportance
            // instruction.exitNumber
            // instruction.turnAngle
          }
        }
      }
    }
  }
}
