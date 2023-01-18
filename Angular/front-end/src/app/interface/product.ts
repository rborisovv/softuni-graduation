export interface Product {
  name: string,
  identifier: string,
  price: number,
  description: string,
  media?: File,
  pkOfFile?: string,
  mediaUrl?: string,
  categoryIdentifier: string,
  categoryMediaUrl?: string
}
