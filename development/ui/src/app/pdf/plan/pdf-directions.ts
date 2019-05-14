import {List} from "immutable";
import * as JsPdf from "jspdf";
import {PlannerService} from "../../map/planner.service";
import {PlanInstruction} from "../../map/planner/plan/plan-instruction";
import {BitmapIconService} from "../bitmap-icon.service";
import {PdfPage} from "./pdf-page";
import {PdfSideBar} from "./pdf-side-bar";

export class PdfDirections {

  private readonly instructionsPerPage = 19;
  private readonly instructionHeight = (PdfPage.yContentsBottom - PdfPage.yContentsTop) / this.instructionsPerPage;

  private readonly doc = new JsPdf();

  constructor(private instructions: List<PlanInstruction>,
              private iconService: BitmapIconService,
              private plannerService: PlannerService) {
  }

  print(): void {
    this.draw();
    this.doc.save("knooppuntnet.pdf");
  }

  private draw() {

    let pageCount = Math.floor(this.instructions.size / this.instructionsPerPage);
    if ((this.instructions.size % this.instructionsPerPage) > 0) {
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
        rowCount = this.instructions.size % this.instructionsPerPage;
      }


      let x = 30;
      const circleRadius = 5;


      for (let rowIndex = 0; rowIndex < rowCount; rowIndex++) {

        const instructionIndex = (this.instructionsPerPage * pageIndex) + rowIndex;

        if (instructionIndex < this.instructions.size) {

          const instruction = this.instructions.get(instructionIndex);

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
              this.iconService.getIcon(instruction.command).subscribe(icon => {
                this.doc.addImage(icon, "PNG", x, yy, 80, 80);
              });
            }

            const xx = x + (circleRadius * 2) + PdfPage.spacer;
            this.doc.setFontSize(10);

            let text = "";

            if (!!instruction.heading) {

              text = text +
                this.plannerService.translate("head") +
                " " +
                this.plannerService.translate("heading-" + instruction.heading);

              if (!!instruction.street) {
                text = text +
                  " " +
                  this.plannerService.translate("onto") +
                  " " +
                  instruction.street;
              }
            } else {
              const key = "command-" + instruction.command + (!!instruction.street ? "-street" : "");
              text = text + this.plannerService.translate(key);
              if (!!instruction.street) {
                text = text + " " + instruction.street;
              }
            }

            this.doc.text(text, xx, yy, {baseline: "top", lineHeightFactor: "1"});
            yy += 5;

            this.doc.text(instruction.distance + " m", xx, yy, {baseline: "top", lineHeightFactor: "1"});
            yy += 5;
          }
        }
      }
    }
  }
}
