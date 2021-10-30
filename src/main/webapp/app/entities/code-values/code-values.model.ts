import { ICodeTables } from 'app/entities/code-tables/code-tables.model';

export interface ICodeValues {
  id?: number;
  key?: string;
  value?: string | null;
  codeTables?: ICodeTables | null;
}

export class CodeValues implements ICodeValues {
  constructor(public id?: number, public key?: string, public value?: string | null, public codeTables?: ICodeTables | null) {}
}

export function getCodeValuesIdentifier(codeValues: ICodeValues): number | undefined {
  return codeValues.id;
}
