**CPA数据抓取项目**

使用多线程维护数据抓取业务，启动时即可运行。
完成一次数据抓取后，会根据配置文件中的``api.heartbeat``的时间(单位分钟)重启。
当有失败的任务时，会在第二天上午9:00将错误日志发送到相应人员邮箱中。