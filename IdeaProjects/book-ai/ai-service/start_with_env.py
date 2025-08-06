#!/usr/bin/env python3
import os
import uvicorn

# OpenAI API 키를 환경변수로 설정
os.environ['OPENAI_API_KEY'] = 'your_openai_api_key_here

print(f"Setting OPENAI_API_KEY: {os.environ.get('OPENAI_API_KEY', 'NOT SET')[:20]}...")

if __name__ == "__main__":
    uvicorn.run(
        "app.main:app",
        host="0.0.0.0",
        port=8000,
        reload=False
    )