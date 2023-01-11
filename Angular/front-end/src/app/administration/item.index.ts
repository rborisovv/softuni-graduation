import {ChangeDetectorRef, Component, ElementRef, ViewChild} from "@angular/core";

@Component({
  template: 'CreateCategoryComponent'
})
export abstract class AdminSharedFunc {

  protected constructor(protected changeDetectorRef: ChangeDetectorRef) {
  }

  @ViewChild('mediaInput') mediaInput: ElementRef;

  mediaPath: string;
  imageSrc: string | ArrayBuffer;


  protected handleMediaUploadClick(): void {
    this.mediaInput.nativeElement.click();
  }

  protected onMediaUpload(event: any) {
    this.mediaPath = this.mediaInput.nativeElement.value;
    this.readURL(event);
  }

  protected readURL(event: any): void {
    if (event.target.files && event.target.files[0]) {
      const file = event.target.files[0];

      const reader = new FileReader();
      reader.onload = () => {
        this.imageSrc = reader.result;
        this.changeDetectorRef.markForCheck();
      };

      reader.readAsDataURL(file);
    }
  }
}
