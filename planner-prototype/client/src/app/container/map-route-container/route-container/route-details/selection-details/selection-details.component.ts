import {Component, Input} from "@angular/core";
import {Section} from "../../../../../model";

@Component({
  selector: "app-selection-details",
  templateUrl: "./selection-details.component.html",
  styleUrls: ["./selection-details.component.scss"]
})
export class SelectionDetailsComponent {

  @Input() sections: Section[];

}
