import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICodeTables } from '../code-tables.model';
import { CodeTablesService } from '../service/code-tables.service';
import { CodeTablesDeleteDialogComponent } from '../delete/code-tables-delete-dialog.component';

@Component({
  selector: 'jhi-code-tables',
  templateUrl: './code-tables.component.html',
})
export class CodeTablesComponent implements OnInit {
  codeTables?: ICodeTables[];
  isLoading = false;

  constructor(protected codeTablesService: CodeTablesService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.codeTablesService.query().subscribe(
      (res: HttpResponse<ICodeTables[]>) => {
        this.isLoading = false;
        this.codeTables = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICodeTables): number {
    return item.id!;
  }

  delete(codeTables: ICodeTables): void {
    const modalRef = this.modalService.open(CodeTablesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.codeTables = codeTables;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
