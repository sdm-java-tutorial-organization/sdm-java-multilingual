# Multilingual Excel Convert

###### Java Tutorial Proeject

팀내 다국어 통합 프로세스를 구축하기 위하여 제작한 애플리케이션입니다.
기획 및 운영팀은 통합 엑셀파일을 관리하고 각각의 엑셀파일의 배포는 이 애플리케이션을 통하여 이루어 집니다.

## Excel 규칙

첫번째 행은 다국어의 국가명을 나타내며, 첫번째 열은 다국어의 키를 나타내는 특수 행과 열입니다.
Excel을 파싱할때, 첫 번째 행과 첫 번째 열에 공백이 존재한다며 그 곳을 마지막으로 인식하기 때문에 
특수 행과 열에는 공백이 들어가지 않도록 주의해야 합니다.

## Manifest 작성

- deployPath
    - 배포되야하는 경로, 또는 방식
- excelName
    - target 폴더내에 excel 파일 이름
    - 만약 target 폴더내에 excel 파일 이름이 없다면 `NotFoundExcelFileException` 반환
- dirName
    - deploy 폴더내에 배포 폴더 이름
    - 만약 deploy 폴더이름이 중복될 경우 `DuplicateDirectoryException` 반환
- filePrefix
    - 배포파일의 접두어
- dilimeter
    - 배포파일의 구분자 (_ 권장)
- titles
    - 배포파일의 종류
    - 만약 없을 경우 엑셀이 첫번째 행부분이 배포파일의 종류로 설정
    - kr, en, jp, ...
- type
    - 배포파일 타입
    - properties, json, ...

```json
[
  {
    "deployPath" : "deploy_path",
    "excelName" : "excel.xlsx",
    "dirName" : "mydir",
    "filePrefix": "message",
    "dilimeter": "_",
    "titles": ["en", "kr", "jp"],
    "type": "properties"
  }
]
```

## Import 방식

- 애플리케이션 내장
    - 애플리케이션에 내장된 엑셀파일을 가져와 빌드
- Google Drive 연동
    - `Google Drive`에서 엑셀파일을 가져와 빌드
- S3 연동
    - `S3`에서 엑셀파일을 가져와 빌드

## Deploy 방식

- 애플리케이션내 배포
    - 애플리케이션 내에 빌드된 다국어 파일 배포
- S3 연동
    - 정해진 `S3` 경로로 빌드된 다국어 파일 배포