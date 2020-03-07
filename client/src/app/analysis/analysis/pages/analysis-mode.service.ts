import {Injectable} from "@angular/core";
import {BrowserStorageService} from "../../../services/browser-storage.service";
import {BehaviorSubject} from "rxjs";
import {Observable} from "rxjs";
import {map, tap} from "rxjs/operators";

@Injectable()
export class AnalysisModeService {

  private readonly localStorageKey = "analysis-mode";
  private readonly modeSubject: BehaviorSubject<string>;

  constructor(private browserStorageService: BrowserStorageService) {
    let initialMode = browserStorageService.get(this.localStorageKey);
    if (initialMode === null) {
      browserStorageService.set(this.localStorageKey, "network");
      initialMode = "network";
    }
    this.modeSubject = new BehaviorSubject(initialMode);
  }

  mode(): string {
    return this.modeSubject.getValue();
  }

  setMode(mode: string) {
    this.browserStorageService.set(this.localStorageKey, mode);
    this.modeSubject.next(mode);
  }

  link(networkType: string, country: string): Observable<string> {
    const url = "/analysis/" + networkType + "/" + country;
    return this.modeSubject.pipe(map(e => url + (e === "network" ? "/networks" : "")));
  }

}
