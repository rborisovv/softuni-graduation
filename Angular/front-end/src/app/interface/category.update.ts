export interface CategoryUpdate {
  name: string,
  oldName: string,
  identifier: string,
  oldCategoryIdentifier: string,
  superCategoryIdentifier: string,
  media: File,
  mediaUrl?: string
}
