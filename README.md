### secret_keys.xml 파일 설정 방법

이 프로젝트는 민감한 인증 정보를 Git에 포함하지 않도록 구성
따라서 `secret_keys.xml` 파일을 직접 생성해야함

1.Project/config/secret_keys.xml.template 파일을 확인

2.해당 파일을 복사하여 app/src/main/res/values/secret_keys.xml로 저장

3.템플릿의 [YOUR_CLIENT_ID] 등 placeholder를 실제 발급받은 키 값으로 변경

4.이 파일은 Git에 업로드하지 않고, 각자 로컬에서만 유지