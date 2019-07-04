import {Injectable} from "@angular/core";
import {PlannerService} from "../map/planner.service";
import {DirectionsAnalyzer} from "../map/planner/directions/directions-analyzer";
import {Plan} from "../map/planner/plan/plan";
import {BitmapIconService} from "./bitmap-icon.service";
import {PdfDirections} from "./plan/pdf-directions";
import {PdfDocument} from "./plan/pdf-document";
import {PdfStripDocument} from "./plan/pdf-strip-document";

@Injectable()
export class PdfService {

  constructor(private iconService: BitmapIconService,
              private plannerService: PlannerService) {
  }

  printDocument(plan: Plan): void {
    new PdfDocument(plan).print()
  }

  printStripDocument(plan: Plan): void {
    new PdfStripDocument(plan, this.iconService).print();
  }

  printInstructions(plan: Plan): void {
    const instructions = new DirectionsAnalyzer().analyze(plan);
    new PdfDirections(instructions, this.iconService, this.plannerService).print();
  }

}
