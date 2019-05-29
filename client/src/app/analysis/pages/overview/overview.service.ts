import {Injectable} from "@angular/core";
import {List} from "immutable";
import {StatisticConfiguration} from "./statistic-configuration";

@Injectable({
  providedIn: "root"
})
export class OverviewService {
  statisticConfigurations: List<StatisticConfiguration> = null;
}
