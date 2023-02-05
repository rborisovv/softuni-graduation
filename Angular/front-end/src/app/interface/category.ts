import {Product} from "./product";

export interface Category {
  name: string,
  identifier: string,
  superCategory: string,
  media: File,
  mediaUrl?: string,
  pkOfFile?: string,
  products?: Product[],
  breadcrumb?: object
}
