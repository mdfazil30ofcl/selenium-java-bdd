#include <MsgBoxConstants.au3>
If WinActivate("[TITLE:Twilio Authy]", "") = False Then
   Run($CmdLine[1],"", @SW_MINIMIZE)
   WinWaitActive("[TITLE:Twilio Authy]", "", 25)
   Sleep(10000)
   ;Sleep(2000)
   ;MsgBox($MB_SYSTEMMODAL, "Title", "X: " & $aPos[0] & ", Y: " & $aPos[1], 10)
EndIf
Sleep(1000)
Send("{ENTER}")
Sleep(2000)
ControlClick("[TITLE:Twilio Authy]", "", "", "left", 1, 350, 506)
Sleep(2000)
;Local $aPos = WinGetPos("[ACTIVE]")
;WinClose("[CLASS:Chrome_WidgetWin_1]", "")
WinSetState ("[TITLE:Twilio Authy]", "", @SW_MINIMIZE )
Exit