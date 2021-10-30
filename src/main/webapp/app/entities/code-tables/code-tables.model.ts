import { ICodeValues } from 'app/entities/code-values/code-values.model';

export interface ICodeTables {
  id?: number;
  tableName?: string;
  codeValues?: ICodeValues[] | null;
}

export class CodeTables implements ICodeTables {
  constructor(public id?: number, public tableName?: string, public codeValues?: ICodeValues[] | null) {}
}

export function getCodeTablesIdentifier(codeTables: ICodeTables): number | undefined {
  return codeTables.id;
}
