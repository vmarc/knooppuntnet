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
            if (instruction.command != null) {
              this.iconRegistry.getNamedSvgIcon(instruction.command).subscribe(svgElement => {
                const str: string = svgElement.outerHTML;
                const canvas: HTMLCanvasElement = document.createElement("canvas");
                // @ts-ignore
                canvg(canvas, str);
                const imgData = canvas.toDataURL("image/png");
                this.doc.addImage(imgData, "PNG", x, yy, 80, 80);
              });
            }

            const xx = x + (circleRadius * 2) + PdfPage.spacer;
            this.doc.setFontSize(10);

            let text = "";
            // if (instruction.text != null) {
            //   text = text + instruction.text + " ";
            // }
            if (instruction.command == "continue") {
              if (instruction.street) {
                text = text + "Rechtdoor op " + instruction.street;
              } else {
                text = text + "Rechtdoor";
              }
            } else if (instruction.command == "turn-slight-left") {
              if (instruction.street) {
                text = text + "Houd link aan naar " + instruction.street;
              } else {
                text = text + "Houd link aan";
              }
            } else if (instruction.command == "turn-slight-right") {
              if (instruction.street) {
                text = text + "Houd rechts aan naar " + instruction.street;
              } else {
                text = text + "Houd rechts aan";
              }
            } else if (instruction.command == "turn-left") {
              if (instruction.street) {
                text = text + "Linksaf naar " + instruction.street;
              } else {
                text = text + "Linksaf";
              }
            } else if (instruction.command == "turn-right") {
              if (instruction.street) {
                text = text + "Rechtsaf naar " + instruction.street;
              } else {
                text = text + "Rechtsaf";
              }
            } else if (instruction.command == "turn-sharp-left") {
              if (instruction.street) {
                text = text + "Scherp linksaf naar " + instruction.street;
              } else {
                text = text + "Scherp linksaf";
              }
            } else if (instruction.command == "turn-sharp-right") {
              if (instruction.street) {
                text = text + "Scherp rechtsaf naar " + instruction.street;
              } else {
                text = text + "Scherp rechtsaf";
              }
            } else {
              if (instruction.street) {
                text = text + instruction.street + " ";
              }
            }

            this.doc.text(text, xx, yy, {baseline: "top", lineHeightFactor: "1"});
            yy += 5;

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
