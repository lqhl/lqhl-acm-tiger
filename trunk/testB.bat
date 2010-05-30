@echo off
if "%1"=="" goto loop
echo Problem Test Case 
echo Data %1
compile MidtermTestCases\B\B%1.tig
goto end
:loop
for %%i in (1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44) do call %0 %%i
:end