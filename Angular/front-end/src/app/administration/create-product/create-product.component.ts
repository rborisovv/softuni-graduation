import {ChangeDetectionStrategy, Component} from '@angular/core';

@Component({
  selector: 'app-create-product',
  templateUrl: './create-product.component.html',
  styleUrls: ['./create-product.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CreateProductComponent {

}

// @ViewChild('mediaInput') mediaInput: ElementRef;
//
// mediaPath: string;
// imageSrc: string | ArrayBuffer;
// selectedMediaFromPickup: Media;
//
//
// public handleMediaUploadClick(): void {
//   this.mediaInput.nativeElement.click();
// }
//
// public onMediaUpload(event: any) {
//   this.mediaPath = this.mediaInput.nativeElement.value;
//   this.readURL(event);
// }
//
// readURL(event: any): void {
//   if (event.target.files && event.target.files[0]) {
//     const file = event.target.files[0];
//
//     const reader = new FileReader();
//     reader.onload = () => {
//       this.imageSrc = reader.result;
//       this.changeDetectorRef.markForCheck();
//     };
//
//     reader.readAsDataURL(file);
//   }
// }
