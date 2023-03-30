import { User } from "./user";
import { Category } from "./category";

export interface Voucher {
  name: string,
  type: string,
  discount: number,
  creationDate?: Date,
  expirationDate: Date,
  user?: User,
  category?: Category
}
