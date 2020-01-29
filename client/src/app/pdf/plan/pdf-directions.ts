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
  private readonly leftMargin = 30;
  private readonly nodeCircleRadius = 5;

  private readonly doc = new JsPdf();

  constructor(private instructions: List<PlanInstruction>,
              private iconService: BitmapIconService,
              private plannerService: PlannerService) {
  }

  print(): void {
    this.printPages();
    this.doc.save("knooppuntnet.pdf");
  }

  private printPages(): void {
    const pageCount = this.calculatePageCount();
    for (let pageIndex = 0; pageIndex < pageCount; pageIndex++) {
      if (pageIndex > 0) {
        this.doc.addPage();
      }
      this.printSideBar();
      this.printPage(pageIndex, pageCount);
    }
  }

  private calculatePageCount(): number {
    let pageCount = Math.floor(this.instructions.size / this.instructionsPerPage);
    if ((this.instructions.size % this.instructionsPerPage) > 0) {
      pageCount++;
    }
    return pageCount;
  }

  private calculatePageRowCount(pageIndex: number, pageCount: number): number {
    if (pageIndex < pageCount - 1) {
      return this.instructionsPerPage;
    }
    return this.instructions.size % this.instructionsPerPage;
  }

  private printSideBar(): void {
    new PdfSideBar(this.doc).print();
  }

  private printPage(pageIndex: number, pageCount: number): void {
    const rowCount = this.calculatePageRowCount(pageIndex, pageCount);
    for (let rowIndex = 0; rowIndex < rowCount; rowIndex++) {
      const instructionIndex = (this.instructionsPerPage * pageIndex) + rowIndex;
      if (instructionIndex < this.instructions.size) {
        const instruction = this.instructions.get(instructionIndex);
        const y = PdfPage.yContentsTop + (this.instructionHeight * rowIndex);
        this.printInstruction(y, instruction);
      }
    }
  }

  private printInstruction(y: number, instruction: PlanInstruction): void {
    this.printInstructionDivider(y);
    if (!!instruction.node) {
      this.printNode(y, instruction.node);
    } else {
      const yy = y + PdfPage.spacer;
      this.printInstructionIcon(yy, instruction.command);
      this.printInstructionText(yy, instruction);
    }
  }

  private printInstructionDivider(y: number): void {
    this.doc.setDrawColor(230);
    this.doc.setLineWidth(0.05);
    this.doc.line(this.leftMargin, y, PdfPage.width - PdfPage.marginRight, y);
  }

  private printNode(y: number, node: string): void {
    const xCircleCenter = this.leftMargin + this.nodeCircleRadius;
    const yCircleCenter = y + 2.5 + this.nodeCircleRadius;
    this.doc.setLineWidth(0.05);
    this.doc.circle(xCircleCenter, yCircleCenter, this.nodeCircleRadius);
    this.doc.setFontSize(12);
    this.doc.text(node, xCircleCenter, yCircleCenter, {align: "center", baseline: "middle", lineHeightFactor: "1"});
  }

  private instructionText(instruction: PlanInstruction): string {
    let texts = List<string>();
    if (!!instruction.heading) {
      texts = texts.push(this.plannerService.translate("head"));
      texts = texts.push(" ");
      texts = texts.push(this.plannerService.translate("heading-" + instruction.heading));
      if (!!instruction.street) {
        texts = texts.push(" ");
        texts = texts.push(this.plannerService.translate("onto"));
        texts = texts.push(" ");
        texts = texts.push(instruction.street);
      }
    } else {
      const key = "command-" + instruction.command + (!!instruction.street ? "-street" : "");
      texts = texts.push(this.plannerService.translate(key));
      if (!!instruction.street) {
        texts = texts.push(" ");
        texts = texts.push(instruction.street);
      }
    }
    return texts.join("");
  }

  private printInstructionIcon(y: number, command: string): void {
    this.iconService.getIcon(command).subscribe(icon => {
      this.doc.addImage(icon, "PNG", this.leftMargin, y, 80, 80, "", "FAST");
    });
  }

  private printInstructionText(y: number, instruction: PlanInstruction): void {
    this.doc.setFontSize(10);
    const left = this.leftMargin + (this.nodeCircleRadius * 2) + PdfPage.spacer;
    const text = this.instructionText(instruction);
    this.doc.text(text, left, y, {baseline: "top", lineHeightFactor: "1"});
    this.doc.text(instruction.distance + " m", left, y + 5, {baseline: "top", lineHeightFactor: "1"});
  }
}
