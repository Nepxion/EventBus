@echo on
@echo =============================================================
@echo $                                                           $
@echo $                      Nepxion EventBus                     $
@echo $                                                           $
@echo $                                                           $
@echo $                                                           $
@echo $  Nepxion Studio All Right Reserved                        $
@echo $  Copyright (C) 2017-2050                                  $
@echo $                                                           $
@echo =============================================================
@echo.
@echo off

@title Nepxion EventBus
@color 0a

call mvn versions:set -DgenerateBackupPoms=false -DnewVersion=3.0.0

pause