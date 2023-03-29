import {Product} from "../model/product";

export interface HttpResponse {
  statusCode: number,
  status: string,
  reason: string,
  message: string,
  notificationStatus?: string,
  favouriteProducts?: Product[],
  basketProducts?: Product[]
}
