import {Injectable} from "@angular/core";
import {Map} from "immutable";
import {BehaviorSubject} from "rxjs";
import {AppService} from "./app.service";
import {InterpretedPoiConfiguration} from "./components/ol/domain/interpreted-poi-configuration";
import {PoiGroupPreference, PoiPreference, PoiPreferences} from "./poi-preferences";

@Injectable()
export class PoiService {

  changed: BehaviorSubject<boolean> = new BehaviorSubject(null);

  private zoomLevel: number;
  private poiPreferences: PoiPreferences;
  private poiActive = Map<string, boolean>();
  private poiNames: Map<string, string> = null;

  poiConfiguration: BehaviorSubject<InterpretedPoiConfiguration> = new BehaviorSubject(null);

  constructor(private appService: AppService) {
    this.loadPoiConfiguration();
  }

  private loadPoiConfiguration() {
    this.appService.poiConfiguration().subscribe(response => {
      this.poiConfiguration.next(new InterpretedPoiConfiguration(response.result));
      this.initPoiConfig();
    });
  }

  updatePoiNameRegistry(poiElements: HTMLCollection) {
    if (this.poiNames === null) {
      const keysAndValues: Array<[string, string]> = [];
      Array.from(poiElements).forEach(span => {
        const id = span.getAttribute("id");
        const translation = span.textContent;
        keysAndValues.push([id, translation]);
      });
      this.poiNames = Map<string, string>(keysAndValues);
    }
  }

  isPoiNameRegistryUpdated() {
    return this.poiNames !== null;
  }

  name(poiId: string): string {
    return this.poiNames.get(poiId);
  }

  isPoiActive(poiId: string): boolean {
    return this.poiActive.get(poiId, false);
  }

  updateZoomLevel(zoomLevel: number): void {
    this.zoomLevel = zoomLevel;
    this.updatePoiActive();
  }

  isEnabled(): boolean {
    if (this.poiPreferences != null) {
      return this.poiPreferences.enabled;
    }
    return false;
  }

  updateEnabled(enabled: boolean): void {
    if (this.poiPreferences != null) {
      this.poiPreferences.enabled = enabled;
      this.savePoiConfig();
      this.updatePoiActive();
    }
  }

  isGroupEnabled(groupName: string): boolean {
    if (this.poiPreferences != null) {
      const group = this.poiPreferences.groups.get(groupName);
      if (group != null) {
        return group.enabled;
      }
    }
    return false;
  }

  updateGroupEnabled(groupName: string, enabled: boolean): void {
    this.updateGroup(groupName, (group) => group.enabled = enabled);
  }

  updateGroupShowAll(groupName: string) {
    this.updateGroup(groupName, (group) => {
      group.pois.forEach((poi, poiName) => {
        poi.minLevel = 11;
      });
    });
  }

  updateGroupHideAll(groupName: string) {
    this.updateGroup(groupName, (group) => {
      group.pois.forEach((poi, poiName) => {
        poi.minLevel = 0;
      });
    });
  }

  updateGroupDefault(groupName: string) {
    this.updateGroup(groupName, (group) => {
      group.pois.forEach((poi, poiName) => {
        const poiDefinition = this.poiConfiguration.value.poiDefinitionWithName(poiName);
        if (poiDefinition != null) {
          poi.minLevel = poiDefinition.minLevel;
        }
      });
    });
  }

  private updateGroup(groupName: string, action: (PoiConfigGroup) => void) {
    if (this.poiPreferences != null) {
      const group = this.poiPreferences.groups.get(groupName);
      if (group != null) {
        action(group);
        this.savePoiConfig();
        this.updatePoiActive();
      }
    }
  }

  poiLevel(poiId: string): string {
    if (this.poiPreferences != null) {
      const poi = this.poiPreferences.poi(poiId);
      if (poi != null) {
        return "" + poi.minLevel;
      }
    }
    return null;
  }

  updatePoiLevel(poiId: string, minLevel): void {
    const poi = this.poiPreferences.poi(poiId);
    if (poi != null) {
      poi.minLevel = minLevel;
      this.savePoiConfig();
      this.updatePoiActive();
    }
  }

  updatePoiActive(): void {
    if (this.zoomLevel != null && this.poiPreferences != null) {
      let activeChanged = false;
      this.poiPreferences.groups.forEach((group, groupName) => {
          group.pois.forEach((poi, poiName) => {
            const active = this.poiPreferences.enabled &&
              group.enabled &&
              poi.minLevel != 0 &&
              poi.minLevel <= this.zoomLevel;
            if (this.poiActive.get(poiName) !== active) {
              this.poiActive = this.poiActive.set(poiName, active);
              activeChanged = true;
            }
          });
        }
      );

      if (activeChanged) {
        this.changed.next(true);
      }
    }
  }

  private initPoiConfig() {

    // TODO read from localstorage; only create new one from poiConfiguration if nothing in localstorage yet

    const groupEntries: Array<[string, PoiGroupPreference]> = [];
    this.poiConfiguration.value.getGroupDefinitions().forEach(groupDefinition => {
      const poiEntries: Array<[string, PoiPreference]> = [];
      groupDefinition.poiDefinitions.forEach(poiDefinition => {
        poiEntries.push([poiDefinition.name, new PoiPreference(poiDefinition.defaultLevel)]);
      });
      const pois = Map<string, PoiPreference>(poiEntries);
      groupEntries.push([groupDefinition.name, new PoiGroupPreference(true /*introduce enabledDefault*/, pois)]);
    });
    const groups = Map<string, PoiGroupPreference>(groupEntries);
    this.poiPreferences = new PoiPreferences(groups, true);
  }

  private savePoiConfig() {
    // TODO write this.poiConfig to localstorage
    console.log("DEBUG PoiService savePoiConfig " + JSON.stringify(this.poiPreferences, null, 2));
  }

}
