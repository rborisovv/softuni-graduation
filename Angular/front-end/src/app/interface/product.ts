export interface Product {
  name: string,
  identifier: string,
  price: number,
  description: string,
  media?: File,
  pkOfFile?: string,
  categoryIdentifier: string
}
