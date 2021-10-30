import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICodeTables } from '../code-tables.model';
import { CodeTablesService } from '../service/code-tables.service';

@Component({
  templateUrl: './code-tables-delete-dialog.component.html',
})
export class CodeTablesDeleteDialogComponent {
  codeTables?: ICodeTables;

  constructor(protected codeTablesService: CodeTablesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.codeTablesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
