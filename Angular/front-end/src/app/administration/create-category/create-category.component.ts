import {Component, ElementRef, ViewChild} from '@angular/core';

@Component({
  selector: 'app-create-category',
  templateUrl: './create-category.component.html',
  styleUrls: ['./create-category.component.scss']
})
export class CreateCategoryComponent {

  @ViewChild('media') mediaInput: ElementRef;

  mediaPath: string = '';


  public handleMediaUploadClick(): void {
    this.mediaInput.nativeElement.click();
  }

  public onMediaUpload() {
    this.mediaPath = this.mediaInput.nativeElement.value;
  }
}
