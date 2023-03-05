export interface Product {
  name: string,
  identifier: string,
  price: number,
  description: string,
  media?: File,
  stockLevel: number,
  showBuyButton: boolean,
  pkOfFile?: string,
  mediaUrl?: string,
  categoryIdentifier: string,
  categoryMediaUrl?: string,
  categoryName?: string
}
