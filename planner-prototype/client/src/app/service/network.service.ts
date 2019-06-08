import {Injectable} from "@angular/core";
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: "root"
})
export class NetworkService {

  private networkType = new BehaviorSubject("cycling");
  networkObservable = this.networkType.asObservable();

  constructor() {
  }

  changeNetworkType(network: string) {
    this.networkType.next(network);
  }
}
