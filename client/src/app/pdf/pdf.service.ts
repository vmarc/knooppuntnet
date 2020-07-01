import {Injectable} from "@angular/core";
import {PlannerService} from "../map/planner.service";
import {DirectionsAnalyzer} from "../map/planner/directions/directions-analyzer";
import {Plan} from "../kpn/api/common/planner/plan";
import {BitmapIconService} from "./bitmap-icon.service";
import {PdfDirections} from "./plan/pdf-directions";
import {PdfDocument} from "./plan/pdf-document";
import {PdfStripDocument} from "./plan/pdf-strip-document";

@Injectable()
export class PdfService {

  constructor(private iconService: BitmapIconService,
              private plannerService: PlannerService) {
  }

  printDocument(plan: Plan, planUrl: string, name: string): void {
    new PdfDocument(plan, planUrl, name).print();
  }

  printStripDocument(plan: Plan, name: string): void {
    new PdfStripDocument(plan, name, this.iconService).print();
  }

  printInstructions(plan: Plan, name: string): void {
    const instructions = new DirectionsAnalyzer().analyze(plan);
    new PdfDirections(instructions, this.iconService, this.plannerService, name).print();
  }

}
