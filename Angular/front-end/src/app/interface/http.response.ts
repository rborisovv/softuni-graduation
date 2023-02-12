export interface HttpResponse {
  statusCode: number,
  status: string,
  reason: string,
  message: string,
  notificationStatus?: string
}
