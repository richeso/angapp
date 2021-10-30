import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICodeValues } from '../code-values.model';
import { CodeValuesService } from '../service/code-values.service';
import { CodeValuesDeleteDialogComponent } from '../delete/code-values-delete-dialog.component';

@Component({
  selector: 'jhi-code-values',
  templateUrl: './code-values.component.html',
})
export class CodeValuesComponent implements OnInit {
  codeValues?: ICodeValues[];
  isLoading = false;

  constructor(protected codeValuesService: CodeValuesService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.codeValuesService.query().subscribe(
      (res: HttpResponse<ICodeValues[]>) => {
        this.isLoading = false;
        this.codeValues = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICodeValues): number {
    return item.id!;
  }

  delete(codeValues: ICodeValues): void {
    const modalRef = this.modalService.open(CodeValuesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.codeValues = codeValues;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
