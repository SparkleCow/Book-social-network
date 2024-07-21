import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BookResponseDto } from '../../../../services/models';

@Component({
  selector: 'app-book-card',
  templateUrl: './book-card.component.html',
  styleUrl: './book-card.component.css'
})
export class BookCardComponent {
  private _book: BookResponseDto = {};
  private _manage = false;
  private _bookCover: string | undefined;

  get bookCover(): string | undefined {
    if (this._book.cover) {
      return 'data:image/jpg;base64,' + this._book.cover
    }
    return 'https://picsum.photos/1900/800';
  }

  get book(): BookResponseDto {
    return this._book;
  }

  @Input()
  set book(value: BookResponseDto) {
    this._book = value;
  }


  get manage(): boolean {
    return this._manage;
  }

  @Input()
  set manage(value: boolean) {
    this._manage = value;
  }

  @Output() private share: EventEmitter<BookResponseDto> = new EventEmitter<BookResponseDto>();
  @Output() private archive: EventEmitter<BookResponseDto> = new EventEmitter<BookResponseDto>();
  @Output() private addToWaitingList: EventEmitter<BookResponseDto> = new EventEmitter<BookResponseDto>();
  @Output() private borrow: EventEmitter<BookResponseDto> = new EventEmitter<BookResponseDto>();
  @Output() private edit: EventEmitter<BookResponseDto> = new EventEmitter<BookResponseDto>();
  @Output() private details: EventEmitter<BookResponseDto> = new EventEmitter<BookResponseDto>();

  onShare() {
    this.share.emit(this._book);
  }

  onArchive() {
    this.archive.emit(this._book);
  }

  onAddToWaitingList() {
    this.addToWaitingList.emit(this._book);
  }

  onBorrow() {
    this.borrow.emit(this._book);
  }

  onEdit() {
    this.edit.emit(this._book);
  }

  onShowDetails() {
    this.details.emit(this._book);
  }
}
