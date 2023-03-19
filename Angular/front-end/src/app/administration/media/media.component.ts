import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { MediaService } from "../../service/media.service";
import { Observable } from "rxjs";
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import { Store } from "@ngrx/store";
import { deleteMediaAction } from "../../store/action/media.action";
import { MediaPageable } from "../../model/mediaPageable";
import { PageEvent } from "@angular/material/paginator";
import { PageableData } from "../../model/pageable.data";

@Component({
  selector: 'app-media',
  templateUrl: './media.component.html',
  styleUrls: ['./media.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class MediaComponent implements OnInit {
  constructor(private mediaService: MediaService, private readonly store: Store) {
  }

  faTrash = faTrash;
  medias$: Observable<MediaPageable>;
  pageIndex: number = 0;
  pageSize: number = 10;

  ngOnInit(): void {

    const data: PageableData = {
      pageIndex: 0,
      pageSize: 10
    };

    this.medias$ = this.mediaService.fetchAllMedias(data);
  }

  public deleteCategory(pkOfFile: string) {
    this.store.dispatch(deleteMediaAction({ pk: pkOfFile }));
  }

  onPaginationChange(event: PageEvent) {
    const data: PageableData = {
      pageIndex: event.pageIndex,
      pageSize: event.pageSize
    };

    this.pageSize = event.pageSize;
    this.pageIndex = event.pageIndex;
    this.medias$ = this.mediaService.fetchAllMedias(data);
  }
}
