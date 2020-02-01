import {Injectable} from "@angular/core";
import {BrowserStorageService} from "../../services/browser-storage.service";
import {BehaviorSubject} from "rxjs";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

@Injectable()
export class AnalysisModeService {

  isModeNetwork: Observable<boolean>;
  isModeLocation: Observable<boolean>;
  private readonly localStorageKey = "analysis-mode";
  private modeSubject: BehaviorSubject<string>;

  constructor(private browserStorageService: BrowserStorageService) {
    let initialMode = browserStorageService.get(this.localStorageKey);
    if (initialMode === null) {
      browserStorageService.set(this.localStorageKey, "network");
      initialMode = "network";
    }
    this.modeSubject = new BehaviorSubject(initialMode);
    this.isModeNetwork = this.modeSubject.pipe(map(e => e === "network"));
    this.isModeLocation = this.modeSubject.pipe(map(e => e === "location"));
  }

  mode(): Observable<string> {
    return this.modeSubject;
  }

  currentMode(): string {
    return this.modeSubject.getValue();
  }

  setMode(mode: string) {
    this.browserStorageService.set(this.localStorageKey, mode);
    this.modeSubject.next(mode);
  }
}
