import { Product } from "./product";
import { Checkout } from "./checkout";

export interface Order {
  basketProducts: Product[],
  checkout: Checkout
}
