# Co-Labor Deploy

## 목차
1. [Demo](#demo)
2. [도커 환경 설정](#도커-환경-설정)
3. [설치 및 실행](#설치-및-실행)
4. [프로젝트 구조](#프로젝트-구조)
5. [FAQ](#faq)

<br/>

## DEMO
- 데모 페이지: https://colabor.site/
- 데모 비디오: https://youtu.be/n80eBnDeCCo?si=bNq9XH5HFnQKndKz

<br/>


## 도커 환경 설정

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


<br/>


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

<br/>


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

**1. 설치하고 싶은 위치에 다음 명령을 실행하세요.**
```bash
git clone
cd Co-Labor
```


**2. 도커 이미지 다운**
```bash
docker compose pull
```


**3. 도커 서비스 시작**
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
