import {Injectable} from "@angular/core";
import {StatisticConfiguration} from "./statistic-configuration";
import {List} from "immutable";

@Injectable({
  providedIn: 'root'
})
export class OverviewService {
  statisticConfigurations: List<StatisticConfiguration> = null;
}
