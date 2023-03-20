# simple-fe
이 프로젝트 springboot기반 simple-fe project입니다.

OpenFeign client테스틑 위해 사용하며 동작을 확인하기 위해 simple-api 프로젝트와 동시에 실행하여야 합니다.

빌드 및 실행을 위해 jdk(jdk11이상)는 미리 설치되어 있어야 하며 maven wrapper를 통해 다음과 같이 실행하시기 바랍니다.

```bash
#build
./mvnw clean install -DskipTests
#run
./mvnw spring-boot:run
```
