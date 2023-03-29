export interface Voucher {
  name: string,
  type: string,
  discount: number,
  creationDate?: Date,
  expirationDate: Date
}
