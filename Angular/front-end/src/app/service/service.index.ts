export function createFormData(data: any): FormData {
  const formData = new FormData();
  for (let key in data) {
    formData.set(key, data[key]);
  }
  return formData;
}
