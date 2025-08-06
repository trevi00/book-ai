@echo off
echo Starting FastAPI AI Analysis Service for Integration Testing...
echo.

REM Check if Python is installed
python --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Python is not installed or not in PATH
    echo Please install Python 3.8 or higher
    pause
    exit /b 1
)

REM Create virtual environment if it doesn't exist
if not exist venv (
    echo Creating Python virtual environment...
    python -m venv venv
)

REM Activate virtual environment
echo Activating virtual environment...
call venv\Scripts\activate.bat

REM Install dependencies
echo Installing FastAPI dependencies...
pip install -r requirements.txt

REM Start the server
echo.
echo ===========================================
echo  FastAPI AI Analysis Service Starting
echo ===========================================
echo  Server URL: http://127.0.0.1:8000
echo  API Docs:   http://127.0.0.1:8000/docs
echo  Health:     http://127.0.0.1:8000/api/v1/health/
echo ===========================================
echo.

python main.py