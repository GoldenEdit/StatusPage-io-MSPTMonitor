# StatusPage-io-MSPTMonitor

## About

This is a Minecraft plugin that reports server MSPT to a https://statuspage.io/ 'System Metric'.

Could be used for servers that want to:

- Build more trust with their community
- Monitor MSPT on their statuspage

![image](https://github.com/VanillaPlusNet/StatusPage-io-MSPTMonitor/assets/45533337/53d32db9-b07d-46c7-98e0-3297d2638ccf)
https://vanillaplus.statuspage.io/#system-metrics


## Setup & Installation

The plugin is made for `1.20.1+`

1. Download the latest release from the [Releases](https://github.com/VanillaPlusNet/StatusPage-io-MSPTMonitor/releases) tab
2. Drag it into your `/plugins` folder
3. Restart/reload your server
4. Create an account on https://statuspage.io/ if you haven't already
5. Create a new page on your account if you haven't already
6. On the left sidebar, click `Your page > System Metrics`
7. Click `Add a metric`
8. Display name: `Server Milliseconds Per Tick`
9. Display suffix: `mspt`
10. You will then see a script that provides an example implementation, copy `api_key`, `page_id` and `metric_id` to the Minecraft plugins `config.yml`.
11. `/reloadstatuspage` - Requires OP or `statuspage.reload`
12. Your statuspage.io metric should start to receive data, it might take 2-3 mins to update. If it doesn't, enable `debug` in the config (reload the plugin) and look for errors in console.



## Commands

`/reloadstatuspage` | `statuspage.reload` - Reload the config


