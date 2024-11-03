[한글 버전](https://github.com/Co-Labor-Project/Co-Labor-Deploy/blob/main/README.ko.md)


# Co-Labor Deploy

## Table of Contents

1. [Demo](#demo)
2. [Quick Start](#quick-start)
3. [Installation and Execution](#installation-and-execution)
4. [Project Structure](#project-structure)
5. [FAQ](#faq)

<br/>

## DEMO

- Website: https://colabor.site/
- Demo Video: https://youtu.be/n80eBnDeCCo?si=bNq9XH5HFnQKndKz

<br/>

## Quick Start

### Windows

**1. System Requirements**

- Windows 10/11 Pro, Enterprise, or Education (build 15063 or higher)
- WSL2 enabled
- Virtualization enabled (BIOS settings)

**2. Install Docker Desktop**

- Download the installer from [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- Select "Use WSL 2 instead of Hyper-V" during installation
- Restart the system after installation

**3. Install WSL2 (Windows Subsystem for Linux 2)**

```powershell
wsl --install
```
---

### Linux (Ubuntu)

**1. System Requirements**

- Ubuntu 20.04 LTS or higher
- 64-bit version
- User account with sudo privileges

**2. Install Docker**

```sh
# Remove previous versions
sudo apt-get remove docker docker-engine docker.io containerd runc

# Install required packages
sudo apt-get update
sudo apt-get install \
    ca-certificates \
    curl \
    gnupg \
    lsb-release

# Add Docker’s official GPG key
sudo mkdir -m 0755 -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

# Set up the Docker repository
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Install Docker Engine
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# Add current user to the docker group
sudo usermod -aG docker $USER
newgrp docker
```
---

### macOS

**1. System Requirements**

- macOS 11 or later
- Apple Silicon (M1/M2) or Intel CPU

**2. Install Docker Desktop**

- Download the installer from Docker Desktop
- Open the downloaded .dmg file and install it
- Drag Docker.app to the Applications folder

<br/>

## Installation and Execution

**1. Clone the project in the root folder**

```bash
git clone
cd Co-Labor
```

**2. Download Docker images**

```bash
docker compose pull
```

**3. Register the Naver Map API Key in the frontend `.env` file**

- API request endpoints should start with `/api`

**4. Configure backend in `src/main/resources/application.properties`**

- Initial database settings should be as follows:
  - Database name: co_labor
  - MySQL default username and password: root, root123
  - Insert Spring Session table directly into the database:

  ```bash
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
  
- `spring.jpa.hibernate.ddl-auto=validate` should be set to `create` for the first run and `update` or `validate` for subsequent runs.

- Refer to `application.properties.example` and insert required API Key values.

**5. Install Elasticsearch**

- Java is required

1. Add Elasticsearch GPG Key  
   `sudo rpm --import https://artifacts.elastic.co/GPG-KEY-elasticsearch`

2. Add Elasticsearch repository

   echo "[elasticsearch]
   name=Elasticsearch repository for 8.x packages
   baseurl=https://artifacts.elastic.co/packages/8.x/yum
   gpgcheck=1
   gpgkey=https://artifacts.elastic.co/GPG-KEY-elasticsearch
   enabled=0
   autorefresh=1
   type=rpm-md" | sudo tee /etc/yum.repos.d/elasticsearch.repo

3. Install Elasticsearch  
   `sudo yum install --enablerepo=elasticsearch elasticsearch -y`

4. Modify Elasticsearch configuration  
  `sudo vi /etc/elasticsearch/elasticsearch.yml`

   Add or modify the following:

  ```yml
   cluster.name: app_name
   node.name: node-1
   network.host: accessible_address
   http.port: 9200
   discovery.type: single-node
  ```

5. Start and enable Elasticsearch service
  ```sh
   sudo systemctl start elasticsearch
   sudo systemctl enable elasticsearch
  ```

6. Register Elasticsearch documents  
   Download data from https://www.aihub.or.kr/aihubdata/data/view.do?currMenu=&topMenu=&aihubDataSe=data&dataSetSn=71723 and register it.

   Refer to `elasticsearch-controller` for registration.

**6. Start Docker services**

```power shell
docker compose up -d
```

### Access

- Frontend: http://localhost:5173
- Backend API: http://localhost:8080

<br/>

## Project Structure

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

#### Port Conflict - MySQL Port (3306)

- Stop existing MySQL service
  ```bash
  # Windows
  net stop MySQL80
  # Linux
  sudo service mysql stop
  ```
#### Docker Permission Issues (Linux)
```bash
sudo groupadd docker
sudo usermod -aG docker $USER
newgrp docker
```
#### Windows WSL Issues
```bash
PowerShell
wsl --update
```
For incorrect information or questions, please report them via [Issues](https://github.com/Co-Labor-Project/Co-Labor-Deploy/issues) and [PR](https://github.com/Co-Labor-Project/Co-Labor-Deploy/pulls) 💡


