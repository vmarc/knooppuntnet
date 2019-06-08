import {Component, Input} from "@angular/core";
import {PoiInformation} from "../../../../model";

@Component({
  selector: "app-poiinformation",
  templateUrl: "./poiinformation.component.html",
  styleUrls: ["./poiinformation.component.scss"]
})
export class PoiinformationComponent {

  @Input() poiInformation: PoiInformation;

}
