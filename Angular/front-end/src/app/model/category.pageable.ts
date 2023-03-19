import { Category } from "../interface/category";

export interface CategoryPageable {
  content: Category[],
  pageable: {
    offset: number,
    pageSize: number,
    paged: boolean,
    unpaged: boolean,
  },
  last: boolean,
  totalPages: number,
  totalElements: number,
  size: number,
  number: number,
  first: boolean,
  numberOfElements: number,
  empty: boolean
}
