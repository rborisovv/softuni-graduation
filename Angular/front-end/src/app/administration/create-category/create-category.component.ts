import {Component, ElementRef, ViewChild} from '@angular/core';

@Component({
  selector: 'app-create-category',
  templateUrl: './create-category.component.html',
  styleUrls: ['./create-category.component.scss']
})
export class CreateCategoryComponent {

  @ViewChild('media') mediaInput: ElementRef;

  mediaPath: string;
  imageSrc: string | ArrayBuffer;


  public handleMediaUploadClick(): void {
    this.mediaInput.nativeElement.click();
  }

  public onMediaUpload(event: any) {
    this.mediaPath = this.mediaInput.nativeElement.value;
    this.readURL(event);
  }

  readURL(event: any): void {
    if (event.target.files && event.target.files[0]) {
      const file = event.target.files[0];

      const reader = new FileReader();
      reader.onload = () => this.imageSrc = reader.result;

      reader.readAsDataURL(file);
    }
  }
}
