# Co-Labor Deploy

## 목차

1. [Demo](#demo)
2. [Quick Start](#Quick-Start)
3. [설치 및 실행](#설치-및-실행)
4. [프로젝트 구조](#프로젝트-구조)
5. [FAQ](#faq)

<br/>

## DEMO

- 웹페이지: https://colabor.site/
- 데모 비디오: https://youtu.be/n80eBnDeCCo?si=bNq9XH5HFnQKndKz

<br/>

## Quick Start

### Windows

**1. 시스템 요구사항**

- Windows 10/11 Pro, Enterprise 또는 Education (빌드 15063 이상)
- WSL2 기능 활성화
- 가상화 기능 활성화 (BIOS 설정)

**2. Docker Desktop 설치**

- [Docker Desktop](https://www.docker.com/products/docker-desktop/)에서 설치 파일 다운로드
- 설치 중 "Use WSL 2 instead of Hyper-V" 옵션 선택
- 설치 완료 후 시스템 재시작

**3. WSL2 설치 (Windows Subsystem for Linux 2)**

```powershell
# PowerShell 관리자 권한으로 실행
wsl --install
```

---

### Linux (Ubuntu)

**1. 시스템 요구사항**

- Ubuntu 20.04 LTS 이상
- 64비트 버전
- sudo 권한이 있는 사용자 계정

**2. 도커 설치**

```sh
# 이전 버전 제거
sudo apt-get remove docker docker-engine docker.io containerd runc

# 필요한 패키지 설치
sudo apt-get update
sudo apt-get install \
    ca-certificates \
    curl \
    gnupg \
    lsb-release

# Docker의 공식 GPG 키 추가
sudo mkdir -m 0755 -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

# Docker 레포지토리 설정
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Docker 엔진 설치
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# 현재 사용자를 docker 그룹에 추가
sudo usermod -aG docker $USER
newgrp docker
```

---

### MacOS

**1. 시스템 요구사항**

- macOS 11 이상
- Apple Silicon (M1/M2) 또는 Intel CPU

**2. Docker Desktop 설치**

- Docker Desktop에서 설치 파일 다운로드
- 다운로드한 .dmg 파일 실행 및 설치
- Applications 폴더로 Docker.app 드래그

<br/>

## 설치 및 실행

**1. 프로젝트 루트 폴더로 이동 후 clone**

```bash
git clone
cd Co-Labor
```

**2. 도커 이미지 다운**

```bash
docker compose pull
```

**3. 프론트엔드 `.env` 파일에 naver map API Key 등록**

- API 요청 엔드포인트는 `/api` 경로로 시작해야 합니다.

**4. 백엔드 `src/main/resources/application.properties` 설정**

- 데이터베이스 초기 설정은 아래와 같이 해야 합니다.

  - 데이터베이스 이름은 co_laobr입니다.

  - MySQL 초기 username, password는 root, root123입니다.

  - Spring Session table은 데이터베이스에 직접 삽입해야 합니다.

    ```
    CREATE TABLE SPRING_SESSION (
      PRIMARY_ID CHAR(36) NOT NULL,
      SESSION_ID CHAR(36) NOT NULL,
      CREATION_TIME BIGINT NOT NULL,
      LAST_ACCESS_TIME BIGINT NOT NULL,
      MAX_INACTIVE_INTERVAL INT NOT NULL,
      EXPIRY_TIME BIGINT NOT NULL,
      PRINCIPAL_NAME VARCHAR(100),
      CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
    );

    CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
    CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
    CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

    CREATE TABLE SPRING_SESSION_ATTRIBUTES (
      SESSION_PRIMARY_ID CHAR(36) NOT NULL,
      ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
      ATTRIBUTE_BYTES BLOB NOT NULL,
      CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
      CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
    );
    ```

- `spring.jpa.hibernate.ddl-auto=validate` 이 설정은 처음 실행 시 `create`로 실행, 그 후 `update` 혹은 `validate`로 실행해야 합니다.

- `application.properties.example` 참고 후 필요한 API Key 값을 넣어야 합니다.

**5. Elasticsearch 설치**

- 자바가 설치되어 있어야 합니다.

1. Elasticsearch GPG 키 추가  
   `sudo rpm --import https://artifacts.elastic.co/GPG-KEY-elasticsearch`

2. Elasticsearch 리포지토리 추가

   ```
   echo "[elasticsearch]
   name=Elasticsearch repository for 8.x packages
   baseurl=https://artifacts.elastic.co/packages/8.x/yum
   gpgcheck=1
   gpgkey=https://artifacts.elastic.co/GPG-KEY-elasticsearch
   enabled=0
   autorefresh=1
   type=rpm-md" | sudo tee /etc/yum.repos.d/elasticsearch.repo
   ```

3. Elasticsearch 설치  
   `sudo yum install --enablerepo=elasticsearch elasticsearch -y`

4. Elasticsearch 설정 수정  
   `sudo vi /etc/elasticsearch/elasticsearch.yml`

   다음 내용을 추가 또는 수정

   ```
   cluster.name: 어플이름
   node.name: node-1
   network.host: 접근할 주소
   http.port: 9200
   discovery.type: single-node
   ```

5. Elasticsearch 서비스 시작 및 활성화

   ```
   sudo systemctl start elasticsearch
   sudo systemctl enable elasticsearch
   ```

6. Elasticsearch 문서 등록  
   https://www.aihub.or.kr/aihubdata/data/view.do?currMenu=&topMenu=&aihubDataSe=data&dataSetSn=71723 에서 데이터 다운 후 등록

   등록은 `elsticsearch-controller` 참고

**6. 도커 서비스 시작**

```bash
docker compose up -d
```

### 접속

- 프론트엔드: http://localhost:5173
- 백엔드 API: http://localhost:8080

<br/>

## 프로젝트 구조

```bash
Co-Labor/
├── Co-Labor-FE/
│   ├── src/
│   ├── public/
│   └── package.json
│
├── Co-Labor-BE/
│   ├── src/
│   ├── gradle/
│   └── build.gradle
│
└── docker-compose.yml
```

<br/>

## FAQ

#### 포트 충돌 문제- MySQL 포트(3306) 충돌

- 기존 MySQL 서비스 중지

```bash
# Windows
net stop MySQL80

# Linux
sudo service mysql stop
```

#### 도커 권한 문제 (Linux)

```bash
sudo groupadd docker
sudo usermod -aG docker $USER
newgrp docker
```

#### Windows WSL 문제

```powershell
# WSL 업데이트
wsl --update
```
