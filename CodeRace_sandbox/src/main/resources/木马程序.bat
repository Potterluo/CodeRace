@echo off & setlocal enabledelayedexpansion & color 0A & chcp 437

set "hexWid=65" & set "hexHei=43" & REM 101 x 67

for /f "tokens=2 delims=[]" %%a in ('ver') do for /f "tokens=2 delims=. " %%a in ("%%a") do set /a "FullScreen=-((%%a-6)>>31)"
if "%1"=="" (
    for %%a in ( FontSize:00060004 FontFamily:00000030 WindowSize:00%hexHei%00%hexWid% ScreenColors:0000000a CodePage:000001b5 ScreenBufferSize:00%hexHei%00%hexWid% FullScreen:!FullScreen:-=!
    ) do for /f "tokens=1,2 delims=:" %%b in ("%%a") do >nul reg add HKCU\Console\Bezier_CMD /v %%b /t reg_dword /d 0x%%c /f
    start "Bezier_CMD" /max "%ComSpec%" /c %~s0 1 & exit
) else ( >nul reg delete HKCU\Console\Bezier_CMD /f )

set "Path=%SystemRoot%\system32" & for /f "delims==" %%a in ('set') do if /i "%%a" neq "Path" if /i "%%a" neq "hexWid" if /i "%%a" neq "hexHei" set "%%a="

set /a "pixel_w=4, pixel_h=6" & rem FontSize 4X6
set /a "Cols=wid=0x%hexWid%, lines=hei=0x%hexHei%, ctrl_wid=wid * 3 / 3, ctrl_hei=hei * 3 / 3, iMax=wid*hei"
set /a "XC = Cols/2, YC = lines/2"
(for /l %%i in (1 1 !wid!) do set "t= !t!") & (for /l %%i in (1 1 !hei!) do set "scr=!scr!!t!") & set "t="

set "TAB=	" & for /F %%a in ('"prompt $h&for %%b in (1) do rem"')do Set "BS=%%a"
set /a "buffwid = wid, linesWantBackAbove = hei - 1 + 1, cntBS = 2 + (buffwid + 7) / 8 * linesWantBackAbove"
set "BSs=" & for /L %%a in (1 1 !cntBS!) do set "BSs=!BSs!%BS%"
set "aLineBS=" & for /L %%a in (1 1 !wid!) do set "aLineBS=!aLineBS!%BS%"

set "dic=QWERTYUIOPASDFGHJKLZXCVBNM@#$+[]{}" & set "sumLines=30" & rem sumLines ^< lenth of dic, dic: as pen, don't use syntax character

set /a "DotPerLine=15, DotPerLineSQ=DotPerLine*DotPerLine, DotPerLineCube=DotPerLine*DotPerLineSQ"

set /a "pathDensity=50, pathDensitySQ=pathDensity*pathDensity, pathDensityCube=pathDensitySQ*pathDensity"

for /L %%h in (0 1 3) do for %%i in (0 1 3) do ^
set /a "cx%%h_%%i=!random! %% ctrl_wid - (ctrl_wid>>1), cy%%h_%%i=!random! %% ctrl_hei - (ctrl_hei>>1)"


for /L %%j in (0 1 !DotPerLine!) do (
    set /a "tr_%%j=DotPerLine-%%j,tr2_%%j=tr_%%j*tr_%%j,te2_%%j=%%j*%%j,t0_%%j=tr2_%%j*tr_%%j, t1_%%j=3*tr2_%%j*%%j, t2_%%j=3*tr_%%j*te2_%%j, t3_%%j=te2_%%j*%%j"
)

for /L %%# in (0) do (

    for /L %%i in (1 1 !pathDensity!) do (

        REM pens
        set "dic=!dic:~1!!dic:~0,1!" & set "born=!dic:~%sumLines%,1!"

        set /a "tr=pathDensity-%%i,tr2=tr*tr,te2=%%i*%%i,ct0=tr2*tr, ct1=3*tr2*%%i, ct2=3*tr*te2, ct3=te2*%%i"
        for /L %%h in (0 1 3) do (
            set /a "x%%h=(ct0*cx%%h_0+ct1*cx%%h_1+ct2*cx%%h_2+ct3*cx%%h_3)/pathDensityCube, y%%h=(ct0*cy%%h_0+ct1*cy%%h_1+ct2*cy%%h_2+ct3*cy%%h_3)/pathDensityCube"
        )

        REM title %%i/!pathDensity! {!x0!,!y0!},{!x1!,!y1!},{!x2!,!y2!},{!x3!,!y3!}

        for /L %%j in (0 1 !DotPerLine!) do (

            set /a "dx=(t0_%%j*x0+t1_%%j*x1+t2_%%j*x2+t3_%%j*x3)*pixel_h/(pixel_w*DotPerLineCube), dy=(t0_%%j*y0+t1_%%j*y1+t2_%%j*y2+t3_%%j*y3)/DotPerLineCube"

            for %%u in (+ -) do for %%v in (+ -) do (

                set /a "x=XC %%u dx, y=YC %%v dy, inScr=(x-0^x-wid)&(y-0^y-hei)"

                if !inScr! lss 0 (
                    set /a "ind=x+y*wid+1, lenL=ind-1"
                    for /f "tokens=1,2" %%a in ("!lenL! !ind!") do (set scr=!scr:~0,%%a!!born!!scr:~%%b!)
                )
            )
        )

        REM clear old line
        for %%c in ("!dic:~0,1!") do set "scr=!scr:%%~c= !"

        <nul set /p "=!aLineBS!" & (2>nul echo;%TAB%!BSs!) & <nul set /p "=%BS%"
        <nul set /p "=%BS%!scr:~0,-1!"
    )

    for /L %%h in (0 1 3) do (
        set /a "cx%%h_0=cx%%h_3, cy%%h_0=cy%%h_3, cx%%h_1 = (cx%%h_3 << 1) - cx%%h_2, cy%%h_1 = (cy%%h_3 << 1) - cy%%h_2"
        for %%i in (2 3) do set /a "cx%%h_%%i=!random! %% ctrl_wid - (ctrl_wid>>1), cy%%h_%%i=!random! %% ctrl_hei - (ctrl_hei>>1)"
    )
)
>nul pause
exit