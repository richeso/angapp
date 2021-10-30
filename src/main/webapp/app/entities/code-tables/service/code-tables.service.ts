import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICodeTables, getCodeTablesIdentifier } from '../code-tables.model';

export type EntityResponseType = HttpResponse<ICodeTables>;
export type EntityArrayResponseType = HttpResponse<ICodeTables[]>;

@Injectable({ providedIn: 'root' })
export class CodeTablesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/code-tables');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(codeTables: ICodeTables): Observable<EntityResponseType> {
    return this.http.post<ICodeTables>(this.resourceUrl, codeTables, { observe: 'response' });
  }

  update(codeTables: ICodeTables): Observable<EntityResponseType> {
    return this.http.put<ICodeTables>(`${this.resourceUrl}/${getCodeTablesIdentifier(codeTables) as number}`, codeTables, {
      observe: 'response',
    });
  }

  partialUpdate(codeTables: ICodeTables): Observable<EntityResponseType> {
    return this.http.patch<ICodeTables>(`${this.resourceUrl}/${getCodeTablesIdentifier(codeTables) as number}`, codeTables, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICodeTables>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICodeTables[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCodeTablesToCollectionIfMissing(
    codeTablesCollection: ICodeTables[],
    ...codeTablesToCheck: (ICodeTables | null | undefined)[]
  ): ICodeTables[] {
    const codeTables: ICodeTables[] = codeTablesToCheck.filter(isPresent);
    if (codeTables.length > 0) {
      const codeTablesCollectionIdentifiers = codeTablesCollection.map(codeTablesItem => getCodeTablesIdentifier(codeTablesItem)!);
      const codeTablesToAdd = codeTables.filter(codeTablesItem => {
        const codeTablesIdentifier = getCodeTablesIdentifier(codeTablesItem);
        if (codeTablesIdentifier == null || codeTablesCollectionIdentifiers.includes(codeTablesIdentifier)) {
          return false;
        }
        codeTablesCollectionIdentifiers.push(codeTablesIdentifier);
        return true;
      });
      return [...codeTablesToAdd, ...codeTablesCollection];
    }
    return codeTablesCollection;
  }
}
