import {Injectable} from "@angular/core";
import {MatIconRegistry} from "@angular/material";
import {DirectionsAnalyzer} from "../map/planner/directions/directions-analyzer";
import {Plan} from "../map/planner/plan/plan";
import {PdfDirections} from "./plan/pdf-directions";
import {PdfHorizontal} from "./plan/pdf-horizontal";
import {PdfVertical} from "./plan/pdf-vertical";

@Injectable()
export class PdfService {

  constructor(private iconRegistry: MatIconRegistry) {
  }

  printVertical(plan: Plan): void {
    new PdfVertical(plan).print();
  }

  printHorizontal(plan: Plan): void {
    new PdfHorizontal(plan).print()
  }

  printInstructions(plan: Plan): void {
    const instructions = new DirectionsAnalyzer().analyze(plan);
    new PdfDirections(instructions, this.iconRegistry).print();
  }

}
