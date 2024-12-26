# 2024_DANPOONG_TEAM_3_OutOfCity_BE 

# 프로젝트 설명 및 회고
[Go to velog post](https://velog.io/@5hseok/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9A%8C%EA%B3%A0-2024-%EA%B5%AC%EB%A6%84%ED%86%A4-%EC%9C%A0%EB%8B%88%EB%B8%8C-%EB%8B%A8%ED%92%8D%ED%86%A4-3%ED%8C%80-OutOfCity-BE-%ED%9A%8C%EA%B3%A0)

---
### **브랜치 컨벤션**

- `main` : default 브랜치
- `기능을 개발하면서 각자가 사용할 브랜치`
    - `[feat]` : 기능 추가
    - `[fix]` : 에러 수정, 버그 수정
    - `[refactor]` : 코드 리펙토링 (기능 변경 없이 코드만 수정할 때)
    - `[modify]` : 코드 수정 (기능의 변화가 있을 때)
    - `[chore]` : gradle 세팅, 위의 것 이외에 거의 모든 것
    - 예시 : `feat/#3-social_login_api` ([ ]/#이슈번호-구현하려는 기능)

### **커밋 컨벤션**

- `[CHORE]` : 동작에 영향 없는 코드 or 변경 없는 변경사항(주석 추가 등) or 파일명, 폴더명 수정 or 파일, 폴더 삭제 or 디렉토리 구조 변경
- `[FEAT]` : 새로운 기능 구현
- `[ADD]` : Feat 이외의 부수적인 코드 추가, 라이브러리 추가, 새로운 파일 생성
- `[FIX]` : 코드 수정, 버그/오류 해결
- `[DEL]` : 쓸모없는 코드 삭제
- `[DOCS]` : README나 WIKI 등의 문서 수정
- `[REFACTOR]` : 전면 수정, 코드 리팩토링
- `[MERGE]`: 다른 브랜치와 병합
- `[DEPLOY]`: 배포 관련
    - 예시 : `ex ) git commit -m "[FEAT] 회원가입 기능 완료 #이슈번호"`

### **이슈 & PR 컨벤션**

- `[FEAT]` : 기능 추가
- `[FIX]` : 에러 수정, 버그 수정
- `[CHORE]` : gradle 세팅, 위의 것 이외에 거의 모든 것
- `[DOCS]` : README, 문서
- `[REFACTOR]` : 코드 리펙토링 (기능 변경 없이 코드만 수정할 때)
- `[MODIFY]` : 코드 수정 (기능의 변화가 있을 때)
- **이슈/PR 생성 시 제목 형식은 위의 형식을 따를 것! → Assignees / Labels 선택**
