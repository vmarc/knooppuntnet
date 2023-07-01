import { SymbolImage } from './symbol-image';

export interface SymbolDescription {
  waycolor: string;
  background?: SymbolImage;
  foreground?: SymbolImage;
  foreground2?: SymbolImage;
  text?: string;
  textcolor?: string;
}
