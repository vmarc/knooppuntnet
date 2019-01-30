import {PoiDefinitionGroup} from "./poi-definition-group";
import {List} from "immutable";
import {PoiDefinition} from "./poi-definition";


export class PoiConfiguration {

  constructor(readonly groups: List<PoiDefinitionGroup>) {
  }


  public static get() {

    return new PoiConfiguration(
      List.of(
        new PoiDefinitionGroup(
          "",
          List.of(
            new PoiDefinition("", 0)
          )
        ),
        new PoiDefinitionGroup(
          "",
          List.of(
            new PoiDefinition("", 0)
          )
        ),
        new PoiDefinitionGroup(
          "",
          List.of(
            new PoiDefinition("", 0)
          )
        ),
        new PoiDefinitionGroup(
          "",
          List.of(
            new PoiDefinition("", 0)
          )
        ),
        new PoiDefinitionGroup(
          "",
          List.of(
            new PoiDefinition("", 0)
          )
        ),
        new PoiDefinitionGroup(
          "",
          List.of(
            new PoiDefinition("", 0)
          )
        ),
        new PoiDefinitionGroup(
          "",
          List.of(
            new PoiDefinition("", 0)
          )
        ),

      )
    );
  }


}

