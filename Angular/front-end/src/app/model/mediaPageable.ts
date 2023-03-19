import { Media } from "../interface/media";

export interface MediaPageable {
  content: Media[],
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
