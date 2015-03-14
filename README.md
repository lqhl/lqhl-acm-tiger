# Introduction

This is a homework project for Project Workshop of Compiler Principles, 08 ACM Honored Class at Shanghai Jiao Tong University.

* Author: lqhl (Qin Liu) ( lqgy2001@gmail.com ).
* Exported from <https://code.google.com/p/just-another-malware-analyzer> on 14 Mar, 2015

# 进度

基本模块全部完成，期中部分和期末部分都通过全部测试数据。期末测试数据中`F9_Spill2.tig`编译时间过长，需55s。

实现的优化：

* 局部公共子表达式消除
* 局部复写传播
* 全局死代码删除
* 常量传播
* 函数内联
* 强度削弱
* 常数折叠
* 在IR树上对IfExp、ForExp和WhileExp的翻译进行了优化

# 使用

* `make.bat`生成`.class`文件
* 执行`compiler.bat+文件名`生成汇编代码，如`compiler.bat test.tig`。
* 注意：文件名必须以`.tig`结尾。

