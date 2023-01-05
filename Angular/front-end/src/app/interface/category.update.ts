export interface CategoryUpdate {
  name: string,
  oldName: string,
  identifier: string,
  oldCategoryIdentifier: string,
  productNamePrefix?: string,
  media: File,
  mediaUrl?: string
}
