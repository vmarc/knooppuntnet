import {Coordinate} from "ol/coordinate";

export class Printer {

  private out = "";

  result(): string {
    return this.out;
  }

  println(message: string): void {
    this.print(message + "\n");
  }

  print(message: string): void {
    this.out += message;
  }

  coordinate(coordinate: Coordinate): string {
    return `[${coordinate[0]},${coordinate[1]}]`;
  }

}
