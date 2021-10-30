import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICodeValues } from '../code-values.model';
import { CodeValuesService } from '../service/code-values.service';

@Component({
  templateUrl: './code-values-delete-dialog.component.html',
})
export class CodeValuesDeleteDialogComponent {
  codeValues?: ICodeValues;

  constructor(protected codeValuesService: CodeValuesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.codeValuesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
