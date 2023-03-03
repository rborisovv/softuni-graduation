import {Product} from "./product";

export interface ICart {
  products: {
    product: Product,
    stockLvl: number
  }
}
