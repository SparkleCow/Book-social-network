import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BookResponseDto } from '../../../../services/models';

@Component({
  selector: 'app-book-card',
  templateUrl: './book-card.component.html',
  styleUrl: './book-card.component.css'
})
export class BookCardComponent {

  @Input() book: BookResponseDto = {};
  @Input() manage = false;

  @Output() private share: EventEmitter<BookResponseDto> = new EventEmitter<BookResponseDto>();
  @Output() private archive: EventEmitter<BookResponseDto> = new EventEmitter<BookResponseDto>();
  @Output() private addToWaitingList: EventEmitter<BookResponseDto> = new EventEmitter<BookResponseDto>();
  @Output() private borrow: EventEmitter<BookResponseDto> = new EventEmitter<BookResponseDto>();
  @Output() private edit: EventEmitter<BookResponseDto> = new EventEmitter<BookResponseDto>();
  @Output() private details: EventEmitter<BookResponseDto> = new EventEmitter<BookResponseDto>();
  
  private _bookCover: string | undefined;

  get bookCover(): string{
    if (this.book.cover) {
      return 'data:image/jpg;base64,' + this.book.cover
    }
    return 'https://picsum.photos/1900/800';
  }

  onShare() {
    this.share.emit(this.book);
  }

  onArchive() {
    this.archive.emit(this.book);
  }

  onAddToWaitingList() {
    this.addToWaitingList.emit(this.book);
  }

  onBorrow() {
    this.borrow.emit(this.book);
  }

  onEdit() {
    this.edit.emit(this.book);
  }

  onShowDetails() {
    this.details.emit(this.book);
  }
}
