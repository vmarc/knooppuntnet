import {Country} from "../shared/country";

export class Countries {

  static nl = new Country("nl");
  static be = new Country("be");
  static de = new Country("de");
  static fr = new Country("fr");

  static all: Array<Country> = [
    Countries.nl,
    Countries.be,
    Countries.de,
    Countries.fr
  ];

}
