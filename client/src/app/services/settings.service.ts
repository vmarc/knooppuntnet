import {Injectable} from "@angular/core";
import {BrowserStorageService} from "./browser-storage.service";
import {Settings} from "./settings";

@Injectable()
export class SettingsService {

  private _settings: Settings;

  constructor(private browserStorageService: BrowserStorageService) {
  }

  get instructions(): boolean {
    return this.settings().instructions;
  }

  set instructions(instructions: boolean) {
    this.updateSettings(new Settings(instructions));
  }

  private settings(): Settings {
    if (!this._settings) {
      const json = this.browserStorageService.get("settings");
      if (json !== null) {
        this._settings = Settings.fromJSON(JSON.parse(json));
      } else {
        this.updateSettings(new Settings(false));
      }
    }
    return this._settings;
  }

  private updateSettings(settings: Settings): void {
    this._settings = settings;
    this.browserStorageService.set("settings", JSON.stringify(this._settings));
  }

}
