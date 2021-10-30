import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICodeTables, CodeTables } from '../code-tables.model';

import { CodeTablesService } from './code-tables.service';

describe('CodeTables Service', () => {
  let service: CodeTablesService;
  let httpMock: HttpTestingController;
  let elemDefault: ICodeTables;
  let expectedResult: ICodeTables | ICodeTables[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CodeTablesService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      tableName: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a CodeTables', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CodeTables()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CodeTables', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          tableName: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CodeTables', () => {
      const patchObject = Object.assign({}, new CodeTables());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CodeTables', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          tableName: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a CodeTables', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCodeTablesToCollectionIfMissing', () => {
      it('should add a CodeTables to an empty array', () => {
        const codeTables: ICodeTables = { id: 123 };
        expectedResult = service.addCodeTablesToCollectionIfMissing([], codeTables);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(codeTables);
      });

      it('should not add a CodeTables to an array that contains it', () => {
        const codeTables: ICodeTables = { id: 123 };
        const codeTablesCollection: ICodeTables[] = [
          {
            ...codeTables,
          },
          { id: 456 },
        ];
        expectedResult = service.addCodeTablesToCollectionIfMissing(codeTablesCollection, codeTables);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CodeTables to an array that doesn't contain it", () => {
        const codeTables: ICodeTables = { id: 123 };
        const codeTablesCollection: ICodeTables[] = [{ id: 456 }];
        expectedResult = service.addCodeTablesToCollectionIfMissing(codeTablesCollection, codeTables);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(codeTables);
      });

      it('should add only unique CodeTables to an array', () => {
        const codeTablesArray: ICodeTables[] = [{ id: 123 }, { id: 456 }, { id: 52590 }];
        const codeTablesCollection: ICodeTables[] = [{ id: 123 }];
        expectedResult = service.addCodeTablesToCollectionIfMissing(codeTablesCollection, ...codeTablesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const codeTables: ICodeTables = { id: 123 };
        const codeTables2: ICodeTables = { id: 456 };
        expectedResult = service.addCodeTablesToCollectionIfMissing([], codeTables, codeTables2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(codeTables);
        expect(expectedResult).toContain(codeTables2);
      });

      it('should accept null and undefined values', () => {
        const codeTables: ICodeTables = { id: 123 };
        expectedResult = service.addCodeTablesToCollectionIfMissing([], null, codeTables, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(codeTables);
      });

      it('should return initial array if no CodeTables is added', () => {
        const codeTablesCollection: ICodeTables[] = [{ id: 123 }];
        expectedResult = service.addCodeTablesToCollectionIfMissing(codeTablesCollection, undefined, null);
        expect(expectedResult).toEqual(codeTablesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
