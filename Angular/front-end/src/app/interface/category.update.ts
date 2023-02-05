export interface CategoryUpdate {
  name: string,
  oldName: string,
  identifier: string,
  oldCategoryIdentifier: string,
  superCategory: string,
  media: File,
  mediaUrl?: string
}
