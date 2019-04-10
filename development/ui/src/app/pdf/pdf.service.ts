import {Injectable} from "@angular/core";
import {Plan} from "../map/planner/plan/plan";
import {PdfHorizontal} from "./plan/pdf-horizontal";
import {PdfVertical} from "./plan/pdf-vertical";

@Injectable()
export class PdfService {

  printVertical(plan: Plan): void {
    new PdfVertical(plan).print();
  }

  printHorizontal(plan: Plan): void {
    new PdfHorizontal(plan).print()
  }

}
