# 简易接入指引

## 简介

这是一款用于前后端分离、SpringBoot项目的权限认证、鉴权框架 相比于shiro的realm会在BeanPostProcessor阶段就被创建，此框架更适合于bean实例阶段的AutoWired; login时自定义认证逻辑

## 配置

配置AuthInterceptor，以及SessionManager

## 匹配规则

* 对同一路径重复定义规则，不会作合并，而是作为有先后顺序的独立规则
* 先代码，后文件
* 先精准，后通配
* 先权限，后匿名
* 其余情况保持原有顺序

## 权限定义

使用“:”分隔“模块名”与“权限名”，使用“*”作为通配符