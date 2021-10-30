import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICodeValues, CodeValues } from '../code-values.model';

import { CodeValuesService } from './code-values.service';

describe('CodeValues Service', () => {
  let service: CodeValuesService;
  let httpMock: HttpTestingController;
  let elemDefault: ICodeValues;
  let expectedResult: ICodeValues | ICodeValues[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CodeValuesService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      key: 'AAAAAAA',
      value: 'AAAAAAA',
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

    it('should create a CodeValues', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CodeValues()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CodeValues', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          key: 'BBBBBB',
          value: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CodeValues', () => {
      const patchObject = Object.assign(
        {
          key: 'BBBBBB',
        },
        new CodeValues()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CodeValues', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          key: 'BBBBBB',
          value: 'BBBBBB',
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

    it('should delete a CodeValues', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCodeValuesToCollectionIfMissing', () => {
      it('should add a CodeValues to an empty array', () => {
        const codeValues: ICodeValues = { id: 123 };
        expectedResult = service.addCodeValuesToCollectionIfMissing([], codeValues);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(codeValues);
      });

      it('should not add a CodeValues to an array that contains it', () => {
        const codeValues: ICodeValues = { id: 123 };
        const codeValuesCollection: ICodeValues[] = [
          {
            ...codeValues,
          },
          { id: 456 },
        ];
        expectedResult = service.addCodeValuesToCollectionIfMissing(codeValuesCollection, codeValues);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CodeValues to an array that doesn't contain it", () => {
        const codeValues: ICodeValues = { id: 123 };
        const codeValuesCollection: ICodeValues[] = [{ id: 456 }];
        expectedResult = service.addCodeValuesToCollectionIfMissing(codeValuesCollection, codeValues);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(codeValues);
      });

      it('should add only unique CodeValues to an array', () => {
        const codeValuesArray: ICodeValues[] = [{ id: 123 }, { id: 456 }, { id: 27987 }];
        const codeValuesCollection: ICodeValues[] = [{ id: 123 }];
        expectedResult = service.addCodeValuesToCollectionIfMissing(codeValuesCollection, ...codeValuesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const codeValues: ICodeValues = { id: 123 };
        const codeValues2: ICodeValues = { id: 456 };
        expectedResult = service.addCodeValuesToCollectionIfMissing([], codeValues, codeValues2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(codeValues);
        expect(expectedResult).toContain(codeValues2);
      });

      it('should accept null and undefined values', () => {
        const codeValues: ICodeValues = { id: 123 };
        expectedResult = service.addCodeValuesToCollectionIfMissing([], null, codeValues, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(codeValues);
      });

      it('should return initial array if no CodeValues is added', () => {
        const codeValuesCollection: ICodeValues[] = [{ id: 123 }];
        expectedResult = service.addCodeValuesToCollectionIfMissing(codeValuesCollection, undefined, null);
        expect(expectedResult).toEqual(codeValuesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
