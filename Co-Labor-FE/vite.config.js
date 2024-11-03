import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig(({ mode }) => {
  // mode에 맞는 .env 파일 로드
  const env = loadEnv(mode, process.cwd());

  return {
    plugins: [react()],
    server: {
      // 경로가 "/api" 로 시작하는 요청을 대상으로 proxy 설정
      proxy: {
        '/api': {
          // 요청 전달 대상 서버 주소 설정
          target: `${env.VITE_SERVER_URL}:80`,
          changeOrigin: true,
          // SSL 인증서 검증 무시
          secure: true,
          // WebSocket 프로토콜 사용
          ws: true,
        },
      },
    },
  };
});
